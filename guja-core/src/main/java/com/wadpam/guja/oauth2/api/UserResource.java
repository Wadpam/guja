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
import com.google.inject.name.Named;
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.oauth2.domain.DUser;
import com.wadpam.guja.oauth2.service.UserService;
import com.wadpam.guja.web.JsonCharacterEncodingResponseFilter;
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
@Produces(JsonCharacterEncodingResponseFilter.APPLICATION_JSON_UTF8)
public class UserResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[-_@\\.\\w]{5,254}$", Pattern.UNICODE_CHARACTER_CLASS);
  private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[\\x21-\\x7E\\xA0-\\xBF\\w]{5,256}$", Pattern.UNICODE_CHARACTER_CLASS);
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

  private UserService userService;

  private boolean shouldUseEmailAsUsername;


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

    LOGGER.debug("Sign up user {}", user.getUsername());

    if (!shouldUseEmailAsUsername) {
      checkUsernameFormat(user.getUsername());
    }
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
    if (null == username || !USERNAME_PATTERN.matcher(username).matches()) {
      LOGGER.info("Invalid username format {}", username);
      throw new BadRequestRestException(String.format("Invalid username format %s", username));
    }
  }

  private static void checkPasswordFormat(String password) {
    if (null == password || !PASSWORD_PATTERN.matcher(password).matches()) {
      LOGGER.info("Invalid password format {}", password);
      throw new BadRequestRestException(String.format("Invalid password format %s", password));
    }
  }

  private static void checkEmailFormat(String email) {
    if (null == email || !EMAIL_PATTERN.matcher(email).matches()) {
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
    Long userId = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    return read(userId);
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
   * Allow a user to delete their own account.
   *
   * @return http 204 is successful
   */
  @DELETE
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response delete(@Context HttpServletRequest request) {
    Long userId = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    return delete(userId);
  }

  /**
   * Delete a user.
   *
   * @param id unique user id
   * @return http 204 is successful
   */
  @DELETE
  @Path("{id}")
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
    // Username and email will never be updated, no need to check format

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
   * @param user  new user info
   * @return http 200
   * The URI of the updated user will be stated in the Location header
   */
  @POST
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response updateMe(@Context HttpServletRequest request,
                           @Context UriInfo uriInfo,
                           @Context SecurityContext securityContext,
                           DUser user) {

    Long userId = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    if (null == user.getId()) {
      user.setId(userId);
    } else if (!user.getId().equals(userId)) {
      throw new BadRequestRestException("User ids does not match");
    }

    return update(userId, uriInfo, securityContext, user);
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

    Long userId = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    CursorPage<DUser> page = userService.getFriendsWith(userId, pageSize, cursorKey);

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
   * Change current users username.
   * The username must be unique
   *
   * @param usernameRequest new username
   * @return 200 if success
   *         409 if username is not unique
   */
  @POST
  @Path("me/username")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response changeUsername(@Context HttpServletRequest request, UsernameRequest usernameRequest) {
    Long userId = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    return changeUsername(userId, usernameRequest);
  }

  /**
   * Change a users username.
   * The username must be unique
   *
   * @param usernameRequest new username
   * @return 200 if success
   *         409 if username is not unique
   */
  @POST
  @Path("{id}/username")
  @RolesAllowed({"ROLE_ADMIN"})
  public Response changeUsername(@PathParam("id") Long id, UsernameRequest usernameRequest) {
    checkUsernameFormat(usernameRequest.username);

    userService.changeUsername(id, usernameRequest.getUsername());

    return Response.ok(id).build();
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
  public Response changePassword(@Context HttpServletRequest request, PasswordRequest passwordRequest) {
    checkPasswordFormat(passwordRequest.getNewPassword());
    checkNotNull(passwordRequest.getOldPassword());

    // Only allow changing your own password
    Long userId = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    userService.changePassword(userId, passwordRequest.oldPassword, passwordRequest.getNewPassword());

    return Response.noContent().build();
  }

  /**
   * Change password using a temporary token.
   * Used during password reset flow.
   *
   * @param userId unique user id
   * @param request newPassword and token
   * @return 204 if success, otherwise 403
   */
  @POST
  @Path("{id}/password")
  @PermitAll
  public Response changePassword(@PathParam("id") Long userId, PasswordRequest request) {
    checkNotNull(userId);
    checkNotNull(request.getToken());
    checkPasswordFormat(request.getNewPassword());

    boolean isSuccess = userService.confirmResetPasswordUsingToken(userId, request.getNewPassword(), request.getToken());
    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
  }

  /**
   * Reset user password by sending out a reset email.
   *
   * @param request users unique email
   * @return http 204
   */
  @POST
  @Path("password/reset")
  @PermitAll
  public Response resetPassword(PasswordRequest request) {
    checkNotNull(request.getEmail());

    userService.resetPassword(request.getEmail());
    return Response.noContent().build();
  }

  /**
   * Confirm a newly create account using a temporary token.
   *
   * @param userId unique user id
   * @param request token
   * @return 204 if success
   * @return 400 if id / token combination is invalid
   */
  @POST
  @Path("{id}/account/confirm")
  @PermitAll
  public Response confirmAccount(@PathParam("id") Long userId, AccountRequest request) {
    checkNotNull(userId);
    checkNotNull(request.getToken());

    boolean isSuccess = userService.confirmAccountUsingToken(userId, request.getToken());
    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
  }

  /**
   * Resend account verification email.
   *
   * @param userId unique user id
   * @return 204 if success
   */
  @POST
  @Path("{id}/account/resend")
  @PermitAll
  public Response resendVerifyAccountEmail(@PathParam("id") Long userId) {
    checkNotNull(userId);

    boolean isSuccess = userService.resendVerifyAccountEmail(userId);
    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
  }

  /**
   * User request changing their email address.
   * The new email is temporarily stored and an email is sent to the new email address for confirmation.
   *
   * @param emailRequest new email address
   * @return 204 if success
   */
  @POST
  @Path("me/email")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response changeEmail(@Context HttpServletRequest request, EmailRequest emailRequest) {
    Long userId = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    return changeEmail(userId, emailRequest);
  }

  /**
   * Admin changing a users email.
   *
   * @param userId unique user id
   * @param request injected
   * @return 204 if success
   */
  @POST
  @Path("{id}/email")
  @RolesAllowed({"ROLE_ADMIN"})
  public Response changeEmail(@PathParam("id") Long userId, EmailRequest request) {
    checkNotNull(userId);
    checkEmailFormat(request.getEmail());

    boolean isSuccess = userService.changeEmailAddress(userId, request.getEmail());
    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
  }

  /**
   * User confirm changing email.
   * The token is verified and the temporary stored email will not be permanently saved as the users email.
   *
   * @param userId unique email
   * @return 204 if success
   */
  @POST
  @Path("{id}/email/confirm")
  @PermitAll
  public Response confirmChangeEmail(@PathParam("id") Long userId, EmailRequest request) {
    checkNotNull(userId);
    checkNotNull(request.getToken());

    boolean isSuccess = userService.confirmEmailAddressChangeUsingToken(userId, request.getToken());
    return isSuccess ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
  }

  @Inject(optional = true)
  public void setUseEmailAsUsername(@Named("app.email.useAsUsername") boolean useEmailAsUsername) {
    this.shouldUseEmailAsUsername = useEmailAsUsername;
  }

  public static class UsernameRequest {

    public UsernameRequest() {
    }

    private String username;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }
  }

  public static class EmailRequest {

    public EmailRequest() {
    }

    private String email;
    private String token;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }
  }

  public static class AccountRequest {

    public AccountRequest() {
    }

    private String token;

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }
  }

  public static class PasswordRequest {

    public PasswordRequest() {
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
