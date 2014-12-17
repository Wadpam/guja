/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.wadpam.guja.oauth2.api.OAuth2UserResource;
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.dao.DOAuth2UserDaoBean;
import com.wadpam.guja.oauth2.domain.DConnection;
import net.sf.mardao.dao.AbstractDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author osandstrom
 */
@Singleton
public class OAuth2Filter implements Filter {

  public static final String NAME_ACCESS_TOKEN = "access_token";
  public static final String NAME_USER_ID = "oauth2user.id";
  public static final String NAME_CONNECTION = "oauth2connection";
  public static final String NAME_ROLES = "oauth2user.roles";
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String PREFIX_OAUTH = "OAuth ";

  static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Filter.class);

  private final Provider<DConnectionDaoBean> connectionDaoProvider;
  private final Provider<DOAuth2UserDaoBean> userDaoProvider;

  @Inject
  public OAuth2Filter(Provider<DConnectionDaoBean> connectionDaoProvider,
                      Provider<DOAuth2UserDaoBean> userDaoProvider) {
    this.connectionDaoProvider = connectionDaoProvider;
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    final String accessToken = getAccessToken(request);
    if (null != accessToken) {
      request.setAttribute(NAME_ACCESS_TOKEN, accessToken);

      final DConnection conn = verifyAccessToken(accessToken);
      if (null != conn) {
        LOGGER.info("Authenticated");
        LOGGER.debug(" userId = {} roles {}", conn.getUserId(), conn.getRoles());

        // User is authenticated
        request.setAttribute(NAME_CONNECTION, conn);
        request.setAttribute(NAME_USER_ID, conn.getUserId());
        request.setAttribute(NAME_ROLES, conn.getRoles());

        AbstractDao.setPrincipalName(null != conn.getUserId() ? conn.getUserId().toString() : null);

        request = new SecurityContextRequestWrapper(request, conn);

      } else {
        LOGGER.info("Unauthorised");
        // TODO Should be return 401 or allow the user to continue as anonymous?
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

    } else {
      LOGGER.info("Anonymous");
      request.setAttribute(NAME_ROLES, OAuth2UserResource.ROLE_ANONYMOUS);
      request = new SecurityContextRequestWrapper(request);

    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }

  private static String getAccessToken(HttpServletRequest request) {
    String accessToken = request.getParameter(NAME_ACCESS_TOKEN);

    // check for header
    if (null == accessToken && null != request.getHeader(HEADER_AUTHORIZATION)) {
      String auth = request.getHeader(HEADER_AUTHORIZATION);
      LOGGER.debug("{}: {}", HEADER_AUTHORIZATION, auth);
      int beginIndex = auth.indexOf(PREFIX_OAUTH);
      if (-1 < beginIndex) {
        return auth.substring(beginIndex + PREFIX_OAUTH.length());
      }
    }

    // check for cookie:
    if (null == accessToken && null != request.getCookies()) {
      LOGGER.debug("Look for cookie");
      for (Cookie c : request.getCookies()) {
        if (NAME_ACCESS_TOKEN.equals(c.getName())) {
          return c.getValue();
        }
      }
    }

    return null;
  }

  public static Long getUserId(HttpServletRequest request) {
    return (Long) request.getAttribute(NAME_USER_ID);
  }

  private DConnection verifyAccessToken(String accessToken) {
    final DConnection conn = connectionDaoProvider.get().findByAccessToken(accessToken);
    if (null == conn) {
      LOGGER.debug("No such access_token {}", accessToken);
      return null;
    }

    // expired?
    if (null != conn.getExpireTime() && conn.getExpireTime().before(new Date())) {
      LOGGER.debug("access_token expired {}", conn.getExpireTime());
      return null;
    }

    return conn;
  }

}
