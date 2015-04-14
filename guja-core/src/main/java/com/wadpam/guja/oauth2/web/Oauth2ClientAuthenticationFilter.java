package com.wadpam.guja.oauth2.web;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wadpam.guja.admintask.AdminTask;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import com.wadpam.guja.oauth2.api.FactoryResource;
import com.wadpam.guja.oauth2.api.requests.ClientCredentials;
import com.wadpam.guja.oauth2.dao.DFactoryDaoBean;
import com.wadpam.guja.oauth2.dao.DFactoryMapper;
import com.wadpam.guja.oauth2.domain.DFactory;
import com.wadpam.guja.environment.ServerEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * A Oauth2 client authentication filter.
 * Authenticate the client using basic authentication.
 *
 * Note! This filter only deal with client authentication. User authentication is handled by the OAuth2AuthorizationResource
 *
 * @author mattiaslevin
 */
@Singleton
public class Oauth2ClientAuthenticationFilter implements Filter, AdminTask {
  public static final Logger LOGGER = LoggerFactory.getLogger(Oauth2ClientAuthenticationFilter.class);

  final static String APPLICATION_JSON_WITH_UTF8_CHARSET = MediaType.APPLICATION_JSON + ";charset=UTF-8";

  public static final String PREFIX_BASIC_AUTHENTICATION = "Basic ";

  public static final String ERROR_INVALID_REQUEST = "invalid_request";
  public static final String ERROR_INVALID_CLIENT = "invalid_client";
  public static final String ERROR_INVALID_GRANT = "invalid_grant";
  public static final String ERROR_UNAUTHORIZED_CLIENT = "unauthorized_client";
  public static final String ERROR_UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
  public static final String ERROR_INVALID_SCOPE = "invalid_scope"; // Not used

  public static final String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";
  public static final String PREFIX_BEARER = "Bearer ";

  private final ObjectMapper objectMapper;
  private final DFactoryDaoBean factoryDao;

  private String basicAuthenticationString;


  @Inject
  public Oauth2ClientAuthenticationFilter(
      ObjectMapper objectMapper, DFactoryDaoBean factoryDaoBean, ServerEnvironment serverEnvironment) {

    this.objectMapper = objectMapper;
    this.factoryDao = factoryDaoBean;

    if (serverEnvironment.isDevEnvironment()) {
      createDefaultFactory();
    }

  }

  private void createDefaultFactory() {
    try {
      // TODO Move values to a property file
      // Overwrite any existing record
      factoryDao.put(DFactoryMapper.newBuilder()
          .id(FactoryResource.PROVIDER_ID_SELF)
          .baseUrl("https://wwww.self.com")
          .clientId("12345")
          .clientSecret("9876")
          .build());
    } catch (IOException e) {
      LOGGER.error("Failed populating factory", e);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Do nothing
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    LOGGER.debug("Oauth2 client authentication");

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    // Either the Authorize header or json body is used to provide the client credentials
    String authHeader = request.getHeader(OAuth2Filter.HEADER_AUTHORIZATION);
    ClientCredentials credentials = null;
    if (request.getContentLength() > 0 &&
        (request.getContentType().startsWith(MediaType.APPLICATION_JSON) ||
            request.getContentType().startsWith(MediaType.APPLICATION_FORM_URLENCODED)) ) {

      HttpBodyRequestWrapper wrappedRequest = new HttpBodyRequestWrapper(request);

      if (request.getContentType().startsWith(MediaType.APPLICATION_JSON)) {

        // Parse JSON body
        credentials = objectMapper.readValue(wrappedRequest.getBody(), ClientCredentials.class);

      } else if (request.getContentType().startsWith(MediaType.APPLICATION_FORM_URLENCODED)) {

        // Parse the form encoded request body. Remember to URL decode the parameters
        Map<String, String> formParams = Splitter.on("&").trimResults().withKeyValueSeparator("=").split(wrappedRequest.getBody());
        formParams = Maps.transformValues(formParams, new Function<String, String>() {
          @Override
          public String apply(String value) {
            try {
              return URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
              LOGGER.error("Not possible to URL decode {}", e);
              return value;
            }
          }
        });

        LOGGER.debug("URL decoded form body {}", formParams);

        credentials = new ClientCredentials();
        credentials.setClient_id(formParams.get("client_id"));
        credentials.setClient_secret((formParams.get("client_secret")));

      }

      // Must wrap the request
      request = wrappedRequest;
    }

    // Check for duplicate authentication methods - invalid request
    if (null != authHeader &&
        null != credentials && null != credentials.getClient_id() && null != credentials.getClient_secret()) {
      LOGGER.info("Bad request - duplicate client authentication credentials");
      // Multiple authentication credentials (400, "invalid_request")
      errorMessage(response, HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_REQUEST);
      return;
    }

    // check for header
    if (null != authHeader) {

      LOGGER.debug("{}: {}", OAuth2Filter.HEADER_AUTHORIZATION, authHeader);
      int beginIndex = authHeader.indexOf(PREFIX_BASIC_AUTHENTICATION);
      if (-1 < beginIndex) {
        String baString = authHeader.substring(beginIndex + PREFIX_BASIC_AUTHENTICATION.length());
        String storedBaString = getBasicAuthenticationString();
        LOGGER.debug("{} equals? {}", baString, storedBaString);
        if (!baString.equals(storedBaString)) {
          LOGGER.info("Unauthorized - invalid client credentials");
          // Unauthorized (401, "invalid_client")
          response.setHeader(HEADER_WWW_AUTHENTICATE, PREFIX_BEARER); // TODO What should be returned
          errorMessage(response, HttpServletResponse.SC_UNAUTHORIZED, ERROR_INVALID_CLIENT);
          return;
        }
      } else {
        // Unsupported client authentication method (401, "invalid_client")
        LOGGER.info("Unauthorized - client authentication method not supported");
        response.setHeader(HEADER_WWW_AUTHENTICATE, PREFIX_BEARER); // TODO What should be returned
        errorMessage(response, HttpServletResponse.SC_UNAUTHORIZED, ERROR_INVALID_CLIENT);
        return;
      }

    } else if (null != credentials) {
      // Check JSON
      LOGGER.debug(String.format("%s: %s, %s", PREFIX_BASIC_AUTHENTICATION, credentials.getClient_id(), credentials.getClient_secret()));

      if (null == credentials.getClient_id() && null == credentials.getClient_secret()) {
        // No client authentication included (401, "invalid_client")
        LOGGER.info("Unauthorized - no client credentials found");
        errorMessage(response, HttpServletResponse.SC_UNAUTHORIZED, ERROR_INVALID_CLIENT);
        return;
      } else if (null == credentials.getClient_id() ^ null == credentials.getClient_secret()) {
        LOGGER.info("Bad request - missing required parameter");
        // Missing client authentication parameter (400, "invalid_request")
        errorMessage(response, HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_REQUEST);
        return;
      } else if (!isCredentialsValid(credentials.getClient_id(), credentials.getClient_secret())) {
        LOGGER.info("Unauthorized - invalid client credentials");
        // Unauthorized (401, "invalid_client")
        errorMessage(response, HttpServletResponse.SC_UNAUTHORIZED, ERROR_INVALID_CLIENT);
        return;
      }

    } else {
      // No client authentication included (401, "invalid_client")
      LOGGER.info("Unauthorized - no client credentials found");
      errorMessage(response, HttpServletResponse.SC_UNAUTHORIZED, ERROR_INVALID_CLIENT);
      return;
    }

    chain.doFilter(request, response);

  }

  private static void errorMessage(HttpServletResponse response, int responseCode, String errorMessage) {
    response.setStatus(responseCode);
    try {
      response.getWriter().write(String.format("{\"error\":\"%s\"}", errorMessage));
      response.setContentType(APPLICATION_JSON_WITH_UTF8_CHARSET);
    } catch (IOException e) {
      LOGGER.error("Failed to write json body {}", e);
    }
  }

  private boolean isCredentialsValid(String clientId, String clientSecret) {
    String basicAuthenticationString = createBasicAuthenticationString(clientId, clientSecret);
    LOGGER.info("Basic {} : {}", basicAuthenticationString, getBasicAuthenticationString());
    return getBasicAuthenticationString().equals(basicAuthenticationString);
  }

  private String createBasicAuthenticationString(String username, String password) {
    return BaseEncoding.base64().encode(String.format("%s:%s", username, password).getBytes());
  }

  public String getBasicAuthenticationString() {
    if (null == basicAuthenticationString) {
      try {
        DFactory factory = factoryDao.get(FactoryResource.PROVIDER_ID_SELF);
        basicAuthenticationString = createBasicAuthenticationString(factory.getClientId(), factory.getClientSecret());
      } catch (IOException e) {
        LOGGER.error("Failed to read basic authentication details from the datastore");
        throw new InternalServerErrorRestException("Failed to read basic authentication details from the datastore");
      }
    }
    return basicAuthenticationString;
  }

  @Override
  public void destroy() {
    // Do nothing
  }


  @Override
  public Object processTask(String taskName, Map<String, String[]> parameterMap) {

    if ("createFactory".equalsIgnoreCase(taskName)) {
      createDefaultFactory();
    }

    return null;
  }

}
