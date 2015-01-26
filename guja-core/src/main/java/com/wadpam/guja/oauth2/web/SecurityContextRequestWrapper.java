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

import com.google.common.collect.Lists;
import com.wadpam.guja.oauth2.api.OAuth2UserResource;
import com.wadpam.guja.oauth2.domain.DConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;
import java.util.Collection;


/**
 * Provide a SecurityContext based on Oauth2 authentication.
 *
 * @author mattiaslevin
 */
public class SecurityContextRequestWrapper extends HttpServletRequestWrapper {

  private final String authType = "OAUTH2_AUTH";
  private final Principal principal;
  private final Collection<String> roles;

  public SecurityContextRequestWrapper(HttpServletRequest request) {
    super(request);
    this.principal = new Principal() {
      @Override
      public String getName() {
        return "anonymous";
      }
    };
    this.roles = Lists.newArrayList(OAuth2UserResource.ROLE_ANONYMOUS);
  }

  public SecurityContextRequestWrapper(HttpServletRequest request, final DConnection connection) {
    super(request);
    this.principal = new Principal() {
      @Override
      public String getName() {
        return connection.getId().toString();
      }
    };
    this.roles = connection.getRoles();
  }

  @Override
  public String getAuthType() {
    return authType;
  }


  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean isUserInRole(String role) {
    return roles.contains(role);
  }
}
