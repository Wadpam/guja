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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wadpam.guja.oauth2.domain.DUser;
import com.wadpam.guja.oauth2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * Collect end-points that require GAE admin rights to run.
 * Typically all system administration features are located here.
 *
 * @author mattiaslevin
 */
@Path("_adm")
@Singleton
@PermitAll // This resource is protected in web.xml. Must be GAE app developer to access this resource.
public class AdminResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(AdminResource.class);


  private final UserService userService;


  @Inject
  public AdminResource(UserService userService) {
    this.userService = userService;
  }


  /**
   * Create a default admin.
   * The admin will only be created of no users exist in the datastore.
   *
   * @return http 201 if successfully created
   * The URI of the created user will be stated in the Location header
   */
  @POST
  @Path("users/admin")
  public Response createDefaultAdminUser(@Context UriInfo uriInfo) {

    DUser admin = userService.createDefaultAdmin();

    // Location of created user resource
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder()
        .path(admin.getId().toString());

    return Response.created(uriBuilder.build())
        .entity(admin.getId())
        .build();

  }

}
