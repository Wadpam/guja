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
import com.google.inject.name.Named;
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import com.wadpam.guja.oauth2.api.requests.RefreshTokenRequest;
import com.wadpam.guja.oauth2.api.requests.RevocationRequest;
import com.wadpam.guja.oauth2.api.requests.UserCredentials;
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.dao.DConnectionMapper;
import com.wadpam.guja.oauth2.domain.DConnection;
import com.wadpam.guja.oauth2.domain.DOAuth2User;
import com.wadpam.guja.oauth2.provider.TokenGenerator;
import com.wadpam.guja.oauth2.provider.UserAuthenticationProvider;
import com.wadpam.guja.oauth2.web.OAuth2Filter;
import com.wadpam.guja.oauth2.web.Oauth2ClientAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

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

  private static final boolean ALWAYS_REVOKE_REFRESH_TOKEN = true;
  private static final int DEFAULT_EXPIRES_IN = 60 * 60 * 24 * 7;  // 1 week

  private static final String PASSWORD_GRANT_TYPE = "password";
  private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
  private static final String TOKEN_TYPE_BEARER = "Bearer";

  private final DConnectionDaoBean connectionDao;
  private final TokenGenerator accessTokenGenerator;
  private final UserAuthenticationProvider authenticationProvider;

  private int tokenExpiresIn = DEFAULT_EXPIRES_IN;


  @Inject
  public OAuth2AuthorizationResource(UserAuthenticationProvider authenticationProvider,
                                     TokenGenerator accessTokenGenerator,
                                     DConnectionDaoBean connectionDao) {

    this.connectionDao = connectionDao;
    this.authenticationProvider = authenticationProvider;
    this.accessTokenGenerator = accessTokenGenerator;
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
      throw new BadRequestRestException(ImmutableMap.of("error", Oauth2ClientAuthenticationFilter.ERROR_UNSUPPORTED_GRANT_TYPE));
    }

    if (null == credentials.getUsername() ||
        null == credentials.getPassword()) {
      throw new BadRequestRestException(ImmutableMap.of("error", Oauth2ClientAuthenticationFilter.ERROR_INVALID_REQUEST));
    }

    DOAuth2User oauth2User = authenticationProvider.authenticate(credentials.getUsername(), credentials.getPassword());
    if (null != oauth2User) {

      // load connection from db async style (likely case is new token for existing user)
      final Iterable<DConnection> existingConnections = connectionDao.queryByProviderUserId(oauth2User.getId().toString());

      DConnection connection = generateConnection(oauth2User, null, null);

      connectionDao.putWithCacheKey(connection.getAccessToken(), connection);

      // Remove expired connections for the user
      removeExpiredConnections(FactoryResource.PROVIDER_ID_SELF, existingConnections);

      return Response.ok(ImmutableMap.builder()
          .put("access_token", connection.getAccessToken())
          .put("refresh_token", connection.getRefreshToken())
          .put("expires_in", tokenExpiresIn)
          .put("token_type", TOKEN_TYPE_BEARER)
          .build())
          .cookie(createCookie(connection.getAccessToken(), tokenExpiresIn))
          .build();

    } else {
      // authentication failed
      throw new BadRequestRestException(ImmutableMap.of("error", Oauth2ClientAuthenticationFilter.ERROR_INVALID_GRANT));
    }

  }

  private DConnection generateConnection(DOAuth2User oauth2User, String profileUrl, String imageUrl) {
   return DConnectionMapper.newBuilder()
       .accessToken(accessTokenGenerator.generate())
       .refreshToken(accessTokenGenerator.generate())
       .providerId(FactoryResource.PROVIDER_ID_SELF)
       .providerUserId(oauth2User.getId().toString())
       .userId(oauth2User.getId())
       .expireTime(calculateExpirationDate(tokenExpiresIn))
       .userRoles(convertRoles(oauth2User.getRoles()))
       .displayName(oauth2User.getDisplayName())
       .imageUrl(imageUrl)
       .profileUrl(profileUrl)
       .build();
  }

  private Date calculateExpirationDate(int expiresInSeconds) {
    return DateTime.now().plusSeconds(expiresInSeconds).toDate();
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

    // Invalidate the old cache key
    connectionDao.invalidateCacheKey(connection.getAccessToken());

    connection.setAccessToken(accessTokenGenerator.generate());
    connection.setExpireTime(calculateExpirationDate(tokenExpiresIn));

    connectionDao.putWithCacheKey(connection.getAccessToken(), connection);

    return Response.ok(ImmutableMap.builder()
        .put("access_token", connection.getAccessToken())
        .put("refresh_token", connection.getRefreshToken())
        .put("expires_in", tokenExpiresIn)
        .build())
        .cookie(createCookie(connection.getAccessToken(), tokenExpiresIn))
        .build();

  }


  /**
   * Revoke a users access_token and refresh_token.
   * https://tools.ietf.org/html/rfc7009
   *
   * @param revocationRequest contains either the access token or refresh token and a token type hint
   * @return will always return http 200
   */
  @POST
  @Path("revoke")
  public Response revoke(RevocationRequest revocationRequest) {
    // Perform all validation here to control the exact error message returned to comply with the Oauth2 standard

    String token = revocationRequest.getToken();
    String tokenHint = revocationRequest.getToken_type_hint();

    if (null != token) {
      // Start with the token type hint but always widen the scope if not found
      if (null == tokenHint || "access_token".equals(tokenHint)) {
        if (!revokeAccessToken(token)) {
          revokeRefreshToken(token);
        }
      } else {
        if (!revokeRefreshToken(token)) {
          revokeAccessToken(token);
        }
      }
    }

    // Always send http 200 according to the specification
    return Response.ok().build();
  }

  private boolean revokeAccessToken(String token) {
    DConnection connection = connectionDao.findByAccessToken(token);
    if (null != connection) {
      if (ALWAYS_REVOKE_REFRESH_TOKEN) {
        // Delete the connection completely
        connectionDao.deleteWithCacheKey(connection.getAccessToken(), connection.getId());
      } else {
        // Invalidate old cache key
        connectionDao.invalidateCacheKey(connection.getAccessToken());
        // Remove the access_token
        // Still allow the user to refresh using the refresh token
        connection.setAccessToken(null);
        try {
          // Do not cache, access token is null
          connectionDao.put(connection);
        } catch (IOException e) {
          LOGGER.error("Failed to update connection {}", e);
          throw new InternalServerErrorRestException("failed to update connection");
        }
      }
      return true;
    } else {
      return false;
    }
  }

  private boolean revokeRefreshToken(String token) {
    DConnection connection = connectionDao.findByRefreshToken(token);
    if (null != connection) {
      // Delete the connection completely
      connectionDao.deleteWithCacheKey(connection.getAccessToken(), connection.getId());
      return true;
    } else {
      return false;
    }
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
      // Do not invalidate the cache, they are harmless since expired and they will get evicted over time
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

  @Inject(optional = true)
  public void setTokenExpiresIn(@Named("app.oauth.tokenExpiresIn") int tokenExpiresIn) {
    this.tokenExpiresIn = tokenExpiresIn;
  }

}
