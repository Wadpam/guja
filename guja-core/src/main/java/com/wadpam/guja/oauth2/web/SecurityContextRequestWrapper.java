package com.wadpam.guja.oauth2.web;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.wadpam.guja.oauth2.api.OAuth2UserResource;
import com.wadpam.guja.oauth2.domain.DConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;
import java.util.Collection;


/**
 * Provide a SecurityContext based on Oauth2 authentication.
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
