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
import com.google.appengine.repackaged.com.google.common.io.BaseEncoding;
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
import java.io.IOException;
import java.util.Map;

/**
 * A Oauth2 client authentication filter.
 * Authenticate the client using basic authentication.
 *
 * @author mattiaslevin
 */
@Singleton
public class Oauth2ClientAuthenticationFilter implements Filter, AdminTask {
  public static final Logger LOGGER = LoggerFactory.getLogger(Oauth2ClientAuthenticationFilter.class);

  public static final String PREFIX_BASIC_AUTHENTICATION = "Basic ";

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

    // check for header
    if (null != request.getHeader(OAuth2Filter.HEADER_AUTHORIZATION)) {

      String auth = request.getHeader(OAuth2Filter.HEADER_AUTHORIZATION);
      LOGGER.debug("{}: {}", OAuth2Filter.HEADER_AUTHORIZATION, auth);
      int beginIndex = auth.indexOf(PREFIX_BASIC_AUTHENTICATION);
      if (-1 < beginIndex) {
        String baString = auth.substring(beginIndex + PREFIX_BASIC_AUTHENTICATION.length());
        String storedBaString = getBasicAuthenticationString();
        LOGGER.debug("{} equals? {}", baString, storedBaString);
        if (!baString.equals(storedBaString)) {
          LOGGER.info("Unauthorized");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }
      }

    } else if (request.getContentLength() > 0) {
      // Check JSON
      BodyRequestWrapper wrappedRequest = new BodyRequestWrapper(request);
      ClientCredentials credentials = objectMapper.readValue(wrappedRequest.getBody(), ClientCredentials.class);
      LOGGER.debug(String.format("%s: %s, %s", PREFIX_BASIC_AUTHENTICATION, credentials.getClient_id(), credentials.getClient_secret()));
      if (null == credentials ||
          null == credentials.getClient_id() ||
          null == credentials.getClient_secret() ||
          !isCredentialsValid(credentials.getClient_id(), credentials.getClient_secret())) {
        LOGGER.info("Unauthorized");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      request = wrappedRequest;

    } else {
      LOGGER.info("Unauthorized (no body)");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    chain.doFilter(request, response);

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
