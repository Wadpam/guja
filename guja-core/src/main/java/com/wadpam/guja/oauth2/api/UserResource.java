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
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.oauth2.domain.DUser;
import com.wadpam.guja.oauth2.service.UserService;
import com.wadpam.guja.oauth2.web.OAuth2Filter;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Resource for managing users.
 *
 * @author mattiaslevin
 */
@Singleton
@Path("api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UserResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[-_@\\.\\w]{5,254}$", Pattern.UNICODE_CHARACTER_CLASS);
  private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[\\x21-\\x7E\\xA0-\\xBF\\w]{5,256}$", Pattern.UNICODE_CHARACTER_CLASS);
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
  @PermitAll
  public Response signup(DUser user, @Context UriInfo uriInfo) {

    LOGGER.debug("Signup user {}", user.getUsername());

    if (null == user.getUsername() ||
        null == user.getPassword() ||
        null == user.getEmail()) {
      throw new BadRequestRestException("Missing mandatory parameters");
    }

    validateUser(user);

    user = userService.signup(user);

    // Location of created user resource
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder()
        .path(user.getId().toString());

    return Response.created(uriBuilder.build())
        .entity(user.getId())
        .build();

  }

  private void validateUser(DUser user) {
    checkUsernameFormat(user.getUsername());
    checkPasswordFormat(user.getPassword());
    checkEmailFormat(user.getEmail());
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
  @RolesAllowed({"ROLE_ADMIN"})
  public Response read(@PathParam("id") Long id) {
    checkNotNull(id);
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
   * Search for users with matching email or username.
   *
   * @param email a partial email address
   * @param username a partial username
   * @return a page of matching users
   */
  @GET
  @Path("search")
  @RolesAllowed({"ROLE_ADMIN"})
  public Response search(@QueryParam("email") String email,
                         @QueryParam("username") String username,
                         @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                         @QueryParam("cursorKey") String cursorKey) {

    final CursorPage<DUser> page;
    if (null != email) {
       page = userService.findMatchingUsersByEmail(email, pageSize, cursorKey);
    } else if (null != username) {
      page = userService.findMatchingUsersByUserName(username, pageSize, cursorKey);
    } else {
      throw new BadRequestRestException("No search key provided");
    }

    return Response.ok(page).build();

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

    CursorPage<DUser> page = userService.readPage(pageSize, cursorKey);

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
    checkNotNull(id);
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
    checkNotNull(id);
    validateUser(user);

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


  /**
   * Get all users added me as friends
   * @param request injected
   * @param pageSize page size
   * @param cursorKey cursor key
   * @return page of users
   */
  @GET
  @Path("friendswith")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response friendsWith(@Context HttpServletRequest request,
                              @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                              @QueryParam("cursorKey") String cursorKey) {

    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    CursorPage<DUser> page = userService.getFriendsWith(id, pageSize, cursorKey);

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
   * Change my password. Both the old and new password must be provided.
   *
   * @param passwordRequest old and new password
   * @return 204 (no content) if successful
   */
  @POST
  @Path("me/password")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response changePassword(@Context HttpServletRequest request, Request passwordRequest) {

    checkPasswordFormat(passwordRequest.getNewPassword());
    checkNotNull(passwordRequest.getOldPassword());

    // Only allow changing your own password
    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    userService.changePassword(id, passwordRequest.oldPassword, passwordRequest.getNewPassword());

    return Response.noContent().build();

  }

  /**
   * Change password using a temporary token. Used during password reset flow.
   *
   * @param userId unique user id
   * @param passwordRequest newPassword and token
   * @return 204 if success, otherwise 403
   */
  @POST
  @Path("{id}/password")
  @PermitAll
  public Response changePassword(@PathParam("id") Long userId, Request passwordRequest) {
    checkNotNull(userId);
    checkNotNull(passwordRequest.getToken());
    checkPasswordFormat(passwordRequest.newPassword);

    boolean isSuccess = userService.changePasswordUsingToken(userId, passwordRequest.getNewPassword(), passwordRequest.getToken());

    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();

  }


  /**
   * Reset user password by sending out a reset email.
   *
   * @param passwordRequest users unique email
   * @return http 204
   */
  @POST
  @Path("password/reset")
  @PermitAll
  public Response resetPassword(Request passwordRequest) {
    checkNotNull(passwordRequest.getEmail());

    userService.resetPassword(passwordRequest.getEmail());

    return Response.noContent().build();

  }

  /**
   * Confirm a users email address using a temporary token.
   * @param userId unique user id
   * @param passwordRequest token
   * @return 204 if success
   * @return 400 if id / token combination is invalid
   */
  @POST
  @Path("{id}/email/confirm")
  @PermitAll
  public Response confirmEmail(@PathParam("id") Long userId, Request passwordRequest) {
    checkNotNull(userId);
    checkNotNull(passwordRequest.getToken());

    boolean isSuccess = userService.confirmEmail(userId, passwordRequest.getToken());
    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();

  }

  /**
   * Resend confirm email.
   * @param userId unique user id
   * @return 204 if success
   */
  @POST
  @Path("{id}/email/resendconfirm")
  @PermitAll
  public Response resendConfirmEmail(@PathParam("id") Long userId) {
    checkNotNull(userId);

    boolean isSuccess = userService.resendConfirmEmail(userId);
    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();

  }


  public static class Request {

    public Request() {
    }

    private String oldPassword;
    private String newPassword;
    private String token;
    private String email;

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

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

}
