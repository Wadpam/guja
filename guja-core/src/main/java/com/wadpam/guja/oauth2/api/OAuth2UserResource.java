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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.oauth2.dao.DOAuth2UserDaoBean;
import com.wadpam.guja.oauth2.domain.DOAuth2User;
import com.wadpam.guja.oauth2.web.JsonCharacterEncodingReponseFilter;
import com.wadpam.guja.oauth2.web.OAuth2Filter;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;


/**
 * Manage an oauth2 user.
 *
 * @author osandstrom
 * @author mattiaslevin
 */
@Path("api/oauth2user")
@Produces(JsonCharacterEncodingReponseFilter.APPLICATION_JSON_UTF8)
@RolesAllowed({"ROLE_ADMIN"})
public class OAuth2UserResource extends CrudResource<DOAuth2User, Long, DOAuth2UserDaoBean> {

  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

  public static final Collection<String> DEFAULT_ROLES_USER = Lists.newArrayList(ROLE_USER);
  public static final Collection<String> DEFAULT_ROLES_ADMIN = Lists.newArrayList(ROLE_ADMIN, ROLE_USER);


  @Inject
  public OAuth2UserResource(DOAuth2UserDaoBean dao) {
    super(dao);
  }

  @GET
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response readMe(@Context HttpServletRequest request) throws IOException {
    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    LOGGER.debug("user id = {}", id);
    return read(id);
  }

  @POST
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response updateMe(@Context HttpServletRequest request, DOAuth2User entity)
      throws URISyntaxException, IOException {
    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    if (null == entity.getId()) {
      entity.setId(id);
    } else if (!entity.getId().equals(id)) {
      throw new BadRequestRestException("User ids does not match");
    }
    return update(id, entity);
  }


}
