package com.wadpam.guja.oauth2.api;

/*
 * #%L
 * guja-core
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.Seconds;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import com.wadpam.guja.exceptions.UnauthorizedRestException;
import com.wadpam.guja.oauth2.api.requests.RefreshTokenRequest;
import com.wadpam.guja.oauth2.api.requests.UserCredentials;
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.dao.DFactoryDaoBean;
import com.wadpam.guja.oauth2.dao.DFactoryMapper;
import com.wadpam.guja.oauth2.domain.DConnection;
import com.wadpam.guja.oauth2.domain.DFactory;
import com.wadpam.guja.oauth2.domain.DOAuth2User;
import com.wadpam.guja.oauth2.provider.TokenGenerator;
import com.wadpam.guja.oauth2.provider.Oauth2UserProvider;
import com.wadpam.guja.environment.ServerEnvironment;
import com.wadpam.guja.oauth2.provider.UserAuthenticationProvider;
import com.wadpam.guja.oauth2.social.SocialProfile;
import com.wadpam.guja.oauth2.social.SocialTemplate;
import com.wadpam.guja.oauth2.web.OAuth2Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An oauth2 implementation support the Resource Owner Password Credential Grant flow.
 * http://tools.ietf.org/html/rfc6749#section-4.3
 * This flow is typically used by trusted client that my touch the resource owners (user) credentials.
 *
 * @author mattiaslevin
 * @author sosandstrom
 */
@Path("oauth")
@Singleton
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class OAuth2AuthorizationResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthorizationResource.class);

  private static final String PASSWORD_GRANT_TYPE = "password";
  private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
  private static final int DEFAULT_EXPIRES_IN = 60 * 60 * 24 * 7;  // 1 week
  private static final String TOKEN_TYPE_BEARER = "Bearer";

  private final DConnectionDaoBean connectionDao;
  private final DFactoryDaoBean factoryDao;

  private final Oauth2UserProvider userProvider;
  private final TokenGenerator accessTokenGenerator;
  private final UserAuthenticationProvider authenticationProvider;


  @Inject
  public OAuth2AuthorizationResource(UserAuthenticationProvider authenticationProvider,
                                     TokenGenerator accessTokenGenerator,
                                     Oauth2UserProvider userProvider,
                                     ServerEnvironment serverEnvironment,
                                     DConnectionDaoBean connectionDao,
                                     DFactoryDaoBean factoryDao) {

    this.connectionDao = connectionDao;
    this.factoryDao = factoryDao;
    this.authenticationProvider = authenticationProvider;
    this.accessTokenGenerator = accessTokenGenerator;
    this.userProvider = userProvider;
  }

  /**
   * Authorize a user (basically a sign in)
   *
   * @param credentials user credentials
   * @return access_token and refresh token if successful.
   * Success response http://tools.ietf.org/html/rfc6749#section-5.1
   * Failure response http://tools.ietf.org/html/rfc6749#section-5.2
   */
  @POST
  @Path("authorize")
  public Response authorize(UserCredentials credentials) {
    // Perform all validation here to control the exact error message returned to comply with the Oauth2 standard

    // The client has been authenticated already in the interceptor

    if (!PASSWORD_GRANT_TYPE.equals(credentials.getGrant_type())) {
      // Unsupported grant type
      throw new BadRequestRestException(ImmutableMap.of("error", "unsupported_grant_type"));
    }

    if (null == credentials.getUsername() ||
        null == credentials.getPassword()) {
      throw new BadRequestRestException(ImmutableMap.of("error", "invalid_request"));
    }

    DOAuth2User oauth2User = authenticationProvider.authenticate(credentials.getUsername(), credentials.getPassword());
    if (null != oauth2User) {

      // load connection from db async style (likely case is new token for existing user)
      final Iterable<DConnection> existingConnections = connectionDao.queryByProviderUserId(oauth2User.getId().toString());

      DConnection connection = generateConnection(oauth2User, null, null);

      put(connection);

      // Remove expired connections for the user
      removeExpiredConnections(FactoryResource.PROVIDER_ID_SELF, existingConnections);

      return Response.ok(ImmutableMap.builder()
          .put("access_token", connection.getAccessToken())
          .put("refresh_token", connection.getRefreshToken())
          .put("expires_in", DEFAULT_EXPIRES_IN)
          .put("token_type", TOKEN_TYPE_BEARER)
          .build())
          .cookie(createCookie(connection.getAccessToken(), DEFAULT_EXPIRES_IN))
          .build();

    } else {
      // authentication failed
      throw new BadRequestRestException(ImmutableMap.of("error", "invalid_grant"));
    }

  }

  private DConnection generateConnection(DOAuth2User oauth2User, String profileUrl, String imageUrl) {
    DConnection connection = new DConnection();
    connection.setAccessToken(accessTokenGenerator.generate());
    connection.setRefreshToken(accessTokenGenerator.generate());
    connection.setProviderId(FactoryResource.PROVIDER_ID_SELF);
    connection.setProviderUserId(oauth2User.getId().toString());
    connection.setUserId(oauth2User.getId());
    connection.setExpireTime(calculateExpirationDate(DEFAULT_EXPIRES_IN));
    connection.setUserRoles(convertRoles(oauth2User.getRoles()));
    connection.setDisplayName(oauth2User.getDisplayName());
    connection.setImageUrl(imageUrl);
    connection.setProfileUrl(profileUrl);

    return connection;
  }

  private Date calculateExpirationDate(int expiresInSeconds) {
    return DateTime.now().plusSeconds(expiresInSeconds).toDate();
  }


  public DConnection put(DConnection connection) {
    try {
      connectionDao.put(connection);
      return connection;
    } catch (IOException e) {
      LOGGER.error("Failed to save connection {}", e);
      throw new InternalServerErrorRestException("Failed to save connection");
    }
  }


  private void delete(DConnection connection) {
    try {
      connectionDao.delete(connection.getId());
    } catch (IOException e) {
      LOGGER.error("Failed to delete connection {}", e);
      throw new InternalServerErrorRestException("Failed to delete connection");
    }
  }


  private NewCookie createCookie(String accessToken, int expiresInSeconds) {
    return new NewCookie(OAuth2Filter.NAME_ACCESS_TOKEN, accessToken, "/api", null, null, expiresInSeconds, false);
  }


  /**
   * Refresh an access_token using the refresh token
   *
   * @param refreshToken refresh token
   * @return @return access_token and refresh token if successful.
   * Success response http://tools.ietf.org/html/rfc6749#section-5.1
   * Failure response http://tools.ietf.org/html/rfc6749#section-5.2
   */
  @POST
  @Path("refresh")
  public Response refreshAccessToken(RefreshTokenRequest refreshToken) {
    // Perform all validation here to control the exact error message returned to comply with the Oauth2 standard

    if (null == refreshToken.getRefresh_token() ||
        null == refreshToken.getGrant_type()) {
      throw new BadRequestRestException(ImmutableMap.of("error", "invalid_request"));
    }

    if (!REFRESH_TOKEN_GRANT_TYPE.equals(refreshToken.getGrant_type())) {
      // Unsupported grant type
      throw new BadRequestRestException(ImmutableMap.of("error", "unsupported_grant_type"));
    }

    DConnection connection = connectionDao.findByRefreshToken(refreshToken.getRefresh_token());
    if (null == connection) {
      throw new BadRequestRestException(ImmutableMap.of("error", "invalid_grant"));
    }

    connection.setAccessToken(accessTokenGenerator.generate());
    connection.setExpireTime(calculateExpirationDate(DEFAULT_EXPIRES_IN));

    put(connection);

    return Response.ok(ImmutableMap.builder()
        .put("access_token", connection.getAccessToken())
        .put("refresh_token", connection.getRefreshToken())
        .put("expires_in", DEFAULT_EXPIRES_IN)
        .build())
        .cookie(createCookie(connection.getAccessToken(), DEFAULT_EXPIRES_IN))
        .build();

  }


  /**
   * Revoke a users access_token and refresh_token.
   * https://tools.ietf.org/html/rfc7009
   *
   * @param token either the access_token or refresh token
   * @return will always return http 200
   */
  @GET
  @Path("revoke")
  public Response revoke(@QueryParam("token") String token) {
    // Perform all validation here to control the exact error message returned to comply with the Oauth2 standard

    if (null != token) {

      // Look both in access_token and refresh_token
      DConnection connection = connectionDao.findByAccessToken(token);
      boolean isAccessTokenType = true;
      if (null == connection) {
        isAccessTokenType = false;
        connection = connectionDao.findByRefreshToken(token);
      }

      // Ignore expiration time
      if (null != connection) {
        if (isAccessTokenType) {
          // Remove the access_token
          // Still allow the user to refresh using the refresh token
          connection.setAccessToken("0");
          put(connection);
        } else {
          // Delete the connection completely
          delete(connection);
        }
      }

    }

    // Always send http 200 according to the specification
    return Response.ok().build();

  }


  /**
   * Validate an access_token.
   * The Oauth2 specification does not specify how this should be done. Do similar to what Google does
   *
   * @param access_token access token to validate. Be careful about using url safe tokens or use url encoding.
   * @return http 200 if success and some basic info about the access_token
   */
  @GET
  @Path("tokeninfo")
  public Response validate(@QueryParam("access_token") String access_token) {
    checkNotNull(access_token);

    DConnection connection = connectionDao.findByAccessToken(access_token);
    LOGGER.debug("Connection {}", connection);
    if (null == connection || hasAccessTokenExpired(connection)) {
      throw new BadRequestRestException("Invalid access_token");
    }

    return Response.ok(ImmutableMap.builder()
        .put("user_id", connection.getUserId())
        .put("expires_in", Seconds.secondsBetween(DateTime.now(), new DateTime(connection.getExpireTime())).getSeconds())
        .build())
        .build();

  }

  private boolean hasAccessTokenExpired(DConnection connection) {
    return new DateTime(connection.getExpireTime()).isBeforeNow();
  }


  /**
   * Remove cookie from the user agent.
   *
   * @return Redirect to requests
   * @throws URISyntaxException
   */
  @GET
  @Path("logout")
  public Response logout() throws URISyntaxException {
    return Response
        .temporaryRedirect(new URI("/"))
        .cookie(createCookie(null, 0))
        .build();
  }

  private void removeExpiredConnections(String providerId, Iterable<DConnection> connections) {

    // find other connections for this user, discard expired
    final ArrayList<Long> expiredTokens = new ArrayList<Long>();

    for (DConnection dc : connections) {
      if (providerId.equals(dc.getProviderId())) {
        // expired? only remove if no refresh token
        if (null == dc.getRefreshToken() && null != dc.getExpireTime() && hasAccessTokenExpired(dc)) {
          expiredTokens.add(dc.getId());
        }
      }
    }

    try {
      connectionDao.delete(expiredTokens);
    } catch (IOException e) {
      LOGGER.error("Failed to delete expired tokens {}", e);
      throw new InternalServerErrorRestException("Failed to delete expired tokens");
    }

  }


  public static String convertRoles(Iterable<String> from) {
    if (null == from) {
      return null;
    }
    final StringBuffer to = new StringBuffer();
    boolean first = true;
    for (String s : from) {
      if (!first) {
        to.append(DConnection.ROLE_SEPARATOR);
      }
      to.append(s.trim());
      first = false;
    }
    return to.toString();
  }
}
