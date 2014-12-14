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
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.oauth2.domain.DUser;
import com.wadpam.guja.oauth2.service.UserService;
import com.wadpam.guja.oauth2.web.OAuth2Filter;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;


/**
 * Resource for managing users.
 *
 * @author mattiaslevin
 */
@Singleton
@Path("api/users")
public class UserResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

  private static final Pattern USERNAME_PATTERN = Pattern.compile("^.{5,}$");
  private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{5,}$");
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

  private UserService userService;


  @Inject
  public UserResource(UserService userService) {
    this.userService = userService;
  }


  /**
   * Sign up a new user.
   *
   * @param user    user info
   * @param uriInfo injected
   * @return http 201 if successfully created
   * The URI of the created user will be stated in the Location header
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @PermitAll
  public Response signup(DUser user, @Context UriInfo uriInfo) {

    LOGGER.debug("Signup user {}", user.getUsername());

    // TODO Change to Jersey validation
    if (null == user.getUsername() ||
        null == user.getPassword() ||
        null == user.getEmail()) {
      throw new BadRequestRestException("Missing mandatory parameters");
    }

    checkUsernameFormat(user.getUsername());
    checkPasswordFormat(user.getPassword());
    checkEmailFormat(user.getEmail());

    user = userService.signup(user);

    // Location of created user resource
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder()
        .path(user.getId().toString());

    return Response.created(uriBuilder.build())
        .entity(user.getId())
        .build();

  }

  private static void checkUsernameFormat(String username) {
    if (!USERNAME_PATTERN.matcher(username).matches()) {
      LOGGER.info("Invalid username format {}", username);
      throw new BadRequestRestException(String.format("Invalid username format %s", username));
    }
  }

  private static void checkPasswordFormat(String password) {
    if (!PASSWORD_PATTERN.matcher(password).matches()) {
      LOGGER.info("Invalid password format {}", password);
      throw new BadRequestRestException(String.format("Invalid password format %s", password));
    }
  }

  private static void checkEmailFormat(String email) {
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      LOGGER.info("Invalid email format {}", email);
      throw new BadRequestRestException(String.format("Invalid email format %s", email));
    }
  }


  /**
   * Get details for a single user.
   *
   * @param id unique user id
   * @return user domain object
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed({"ROLE_ADMIN"})
  public Response read(@PathParam("id") Long id) {
    return Response.ok(userService.getById(id)).build();
  }


  /**
   * Get user info for current user.
   *
   * @param request injected
   * @return user domain object associated with the current user.
   * @throws IOException
   */
  @GET
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response readMe(@Context HttpServletRequest request) throws IOException {
    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    return read(id);
  }


  /**
   * Get a page of users.
   *
   * @param pageSize  Optional. Page size.
   * @param cursorKey Optional. Cursor key
   * @return a page of user domain objects
   */
  @GET
  @RolesAllowed({"ROLE_ADMIN"})
  public Response readPage(@QueryParam("pageSize") @DefaultValue("10") int pageSize,
                           @QueryParam("cursorKey") String cursorKey) {

    CursorPage<DUser> page = userService.readPage(cursorKey, pageSize);

    return Response.ok(page).build();
  }


  /**
   * Delete a user
   *
   * @param id unique user id
   * @return http 204 is successful
   */
  @DELETE
  @Path("id")
  @RolesAllowed({"ROLE_ADMIN"})
  public Response delete(@PathParam("id") Long id) {

    userService.deleteById(id);

    return Response.noContent().build();

  }


  /**
   * Update a user.
   *
   * @param user    user updates
   * @param uriInfo injected
   * @return http 200
   * The URI of the updated user will be stated in the Location header
   */
  @POST
  @Path("{id}")
  @RolesAllowed({"ROLE_ADMIN"})
  public Response update(@PathParam("id") Long id,
                         @Context UriInfo uriInfo,
                         @Context SecurityContext securityContext,
                         DUser user) {

    user = userService.update(id, user, securityContext.isUserInRole(OAuth2UserResource.ROLE_ADMIN));

    UriBuilder uriBuilder = uriInfo.getBaseUriBuilder()
        .path("api/users")
        .path(user.getId().toString());

    return Response.ok()
        .location(uriBuilder.build())
        .entity(user.getId())
        .build();

  }


  /**
   * Update the current user.
   *
   * @param request injected
   * @param uriInfo injected
   * @param entity  new user info
   * @return http 200
   * The URI of the updated user will be stated in the Location header
   */
  @POST
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response updateMe(@Context HttpServletRequest request,
                           @Context UriInfo uriInfo,
                           @Context SecurityContext securityContext,
                           DUser entity) {

    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    if (null == entity.getId()) {
      entity.setId(id);
    } else if (!entity.getId().equals(id)) {
      throw new BadRequestRestException("User ids does not match");
    }

    return update(id, uriInfo, securityContext, entity);
  }


  @GET
  @Path("friendswith")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response friendsWith(@Context HttpServletRequest request,
                              @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                              @QueryParam("cursorKey") String cursorKey) {

    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    CursorPage<DUser> page = userService.getFriendsWith(id, cursorKey, pageSize);

    // Only return basic user information about your friends
    Collection<DUser> users = new ArrayList<>();
    for (DUser user : page.getItems()) {
      DUser limitedUserInfo = new DUser();
      limitedUserInfo.setId(user.getId());
      limitedUserInfo.setUsername(user.getUsername());
      users.add(limitedUserInfo);
    }
    page.setItems(users);

    return Response.ok(page).build();
  }


  /**
   * Change my password.
   * Both the old and new password must be provided.
   *
   * @param passwords old and new password
   * @return 200 (no content) if successful
   */
  @POST
  @Path("me/password")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response changePassword(@Context HttpServletRequest request, Passwords passwords) {

    if (null == passwords.getOldPassword() || null == passwords.getNewPassword()) {
      throw new BadRequestRestException("Must provide both old and new password");
    }

    // Only allow changing your own password
    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    checkPasswordFormat(passwords.getNewPassword());

    userService.changePassword(id, passwords.oldPassword, passwords.getNewPassword());

    return Response.noContent().build();

  }


  private class Passwords {

    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
      return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
      this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
      return newPassword;
    }

    public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
    }
  }

}
