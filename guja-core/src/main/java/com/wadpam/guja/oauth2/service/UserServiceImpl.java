package com.wadpam.guja.oauth2.service;

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

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.wadpam.guja.environment.ServerEnvironment;
import com.wadpam.guja.exceptions.*;
import com.wadpam.guja.i18n.Localization;
import com.wadpam.guja.i18n.PropertyFileLocalizationBuilder;
import com.wadpam.guja.oauth2.api.OAuth2UserResource;
import com.wadpam.guja.oauth2.dao.DUserDaoBean;
import com.wadpam.guja.oauth2.domain.DOAuth2User;
import com.wadpam.guja.oauth2.domain.DUser;
import com.wadpam.guja.oauth2.provider.Oauth2UserProvider;
import com.wadpam.guja.oauth2.provider.PasswordEncoder;
import com.wadpam.guja.oauth2.provider.TemporaryTokenCache;
import com.wadpam.guja.oauth2.provider.UserAuthenticationProvider;
import com.wadpam.guja.service.EmailService;
import com.wadpam.guja.template.RequestScopedVelocityTemplateStringWriterBuilder;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Future;

/**
 * User service implementation based on Mardao.
 *
 * @author mattiaslevin
 */
@Singleton
public class UserServiceImpl implements UserService, UserAuthenticationProvider, Oauth2UserProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final static String DEFAULT_ADMIN_USERNAME = "admin";

  private static final String VELOCITY_TEMPLATE_VERIFY_ACCOUNT = "verify_account.vm";
  private static final String VELOCITY_TEMPLATE_CHANGE_EMAIL = "change_email.vm";
  private static final String VELOCITY_TEMPLATE_RESET_PASSWORD = "reset_password.vm";

  private DUserDaoBean userDao;

  private boolean shouldVerifyAccountCreation = true;
  private boolean shouldUseEmailAsUsername = false;

  private String verifyAccountTemplate = VELOCITY_TEMPLATE_VERIFY_ACCOUNT;
  private String changeEmailTemplate = VELOCITY_TEMPLATE_CHANGE_EMAIL;
  private String resetPasswordTemplate = VELOCITY_TEMPLATE_RESET_PASSWORD;

  private final PasswordEncoder passwordEncoder;
  private final ServerEnvironment severEnvironment;
  private final EmailService emailService;
  private final Provider<RequestScopedVelocityTemplateStringWriterBuilder> templateBuilderProvider;
  private final Provider<PropertyFileLocalizationBuilder> localizationBuilderProvider;
  private final TemporaryTokenCache tokenCache;
  private final Provider<UriInfo> uriInfoProvider;

  @Inject
  public UserServiceImpl(DUserDaoBean userDao,
                         PasswordEncoder passwordEncoder,
                         EmailService emailService,
                         Provider<RequestScopedVelocityTemplateStringWriterBuilder> templateBuilderProvider,
                         Provider<PropertyFileLocalizationBuilder> localizationBuilderProvider,
                         ServerEnvironment severEnvironment,
                         TemporaryTokenCache tokenCache,
                         Provider<UriInfo> uriInfoProvider) {

    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.templateBuilderProvider = templateBuilderProvider;
    this.localizationBuilderProvider = localizationBuilderProvider;
    this.severEnvironment = severEnvironment;
    this.tokenCache = tokenCache;
    this.uriInfoProvider = uriInfoProvider;

    if (severEnvironment.isDevEnvironment()) {
      createDefaultAdmin();
    }

  }

  @Override
  public DUser signup(DUser user) {

    lowercaseEmail(user);

    DUser existingUser;
    if (!shouldUseEmailAsUsername) {
      // Check if username already exists
      existingUser = userDao.findByUsername(user.getUsername()); // TODO Change to asynch request
      if (null != existingUser && existingUser.getState() == DUser.UNVERIFIED_STATE) {
        user.setId(existingUser.getId());
      } else if (null != existingUser) {
        LOGGER.info("Username already taken {}", user.getUsername());
        throw new ConflictRestException("Username already taken");
      }
    }

    // Check if email already exists
    existingUser = userDao.findByEmail(user.getEmail()); // TODO Change to asynch request
    if (null != existingUser && existingUser.getState() == DUser.UNVERIFIED_STATE &&
        (null == user.getId() || user.getId().equals(existingUser.getId()))) {
      // Either no match when finding by username or make sure the email belong to the same user as found when finding by username

      user.setId(existingUser.getId());

    } else if (null != existingUser) {
      LOGGER.info("Email already taken {}", user.getEmail());
      throw new ConflictRestException("Email already taken");
    }

    // encode password before storage
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    if (null == encodedPassword) {
      throw new InternalServerErrorRestException("Failed to encode user password");
    }
    user.setPassword(encodedPassword);

    user.setRoles(OAuth2UserResource.DEFAULT_ROLES_USER);

    // Is email validation enforced?
    user.setState((!shouldVerifyAccountCreation || severEnvironment.isDevEnvironment())
        ? DUser.ACTIVE_STATE : DUser.UNVERIFIED_STATE);

    put(user);

    if (shouldVerifyAccountCreation) {
      // Send email confirmation email
      verifyAccount(user);
    }

    return user;
  }

  private static void lowercaseEmail(DUser user) {
    if (null != user && null != user.getEmail()) {
      user.setEmail(user.getEmail().toLowerCase());
    }
  }

  private enum TokenType {
    ACCOUNT, EMAIL, PASSWORD
  }

  private boolean verifyAccount(DUser user) {

    Localization localization = localizationBuilderProvider.get().build();
    String subject = localization.getMessage("verifyAccount", "Verify account", user.getUsername());

    String token = tokenCache.generateTemporaryToken(tokenKey(user.getId(), TokenType.ACCOUNT), 60 * 60 * 24); // token is valid 24h

    String url = buildConfirmationURL(user.getId(), localization.getLocale(), token, "VerifyAccount");
    LOGGER.debug("verify account url {}", url);

    String body = templateBuilderProvider.get()
        .templateName(verifyAccountTemplate)
        .put("confirmationUrl", url)
        .put("user", user)
        .put("baseUrl", uriInfoProvider.get().getBaseUri().toString())
        .build()
        .toString();

    return emailService.sendEmail(user.getEmail(), user.getDisplayName(), subject, body, true);
  }

  @Override
  public boolean resendVerifyAccountEmail(Long userId) {
    DUser user = getById(userId); // Will throw 404 if not found
    return verifyAccount(user);
  }

  private String buildConfirmationURL(Long userId, Locale locale, String token, String pathComponent) {
    return uriInfoProvider.get().getBaseUriBuilder()
        .path("html")
        .path(pathComponent)
        .path("index.html")
        .queryParam("id", userId)
        .queryParam("token", token)
        .queryParam("language", locale.getLanguage())
        .build()
        .toString();
  }

  private static String tokenKey(Long userId, TokenType tokenType) {
    return String.format("%s-%s", tokenType.ordinal(), userId);
  }

  @Override
  public boolean confirmAccountUsingToken(Long userId, String token) {

    DUser user = getById(userId); // Will throw 404 if not found
    if (user.getState() == DUser.ACTIVE_STATE) {
      // User is already activated.
      // Probably clicked on an old link, just remove the token
      tokenCache.removeToken(tokenKey(userId, TokenType.ACCOUNT));
      return true;
    } else if (tokenCache.validateToken(tokenKey(userId, TokenType.ACCOUNT), token)) {
      if (user.getState() != DUser.LOCKED_STATE) {
        // Only change state if user account is not locked
        user.setState(DUser.ACTIVE_STATE);
        put(user);
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean changeEmailAddress(Long userId, String newEmailAddress) {

    newEmailAddress = newEmailAddress.toLowerCase();

    Future<DUser> futureUser = getAsyncById(userId); // Non blocking

    // Check that email is unique
    DUser user = userDao.findByEmail(newEmailAddress); // Blocking
    if (null != user) {
      LOGGER.info("Email already taken {}", user.getEmail());
      throw new ConflictRestException("Email already taken");
    }

    try {
      user = futureUser.get(); // Blocking
    } catch (Exception e) {
      LOGGER.error("Failed to read async from datastore {}", e);
      throw new InternalServerErrorRestException("Failed to read async from datastore");
    }

    Localization localization = localizationBuilderProvider.get().build();
    String subject = localization.getMessage("changeEmail", "Change email address", user.getUsername());

    String token = tokenCache.generateTemporaryToken(tokenKey(user.getId(), TokenType.EMAIL), 60 * 60 * 24, newEmailAddress); // token is valid 24h

    String url = buildConfirmationURL(user.getId(), localization.getLocale(), token, "ChangeEmail");
    LOGGER.debug("verify new email url {}", url);

    String body = templateBuilderProvider.get()
        .templateName(changeEmailTemplate)
        .put("confirmationUrl", url)
        .put("user", user)
        .put("baseUrl", uriInfoProvider.get().getBaseUri().toString())
        .build()
        .toString();

    return emailService.sendEmail(newEmailAddress, user.getDisplayName(), subject, body, true);
  }

  @Override
  public boolean confirmEmailAddressChangeUsingToken(Long userId, String token) {
    Optional<Object> newEmailAddress = tokenCache.getContextForToken(tokenKey(userId, TokenType.EMAIL), token);
    if (newEmailAddress.isPresent()) {

      Future<DUser> futureUser = getAsyncById(userId); // Non blocking

      // Check that email is unique
      // Might be someone else that registered the email since the confirmation email was sent out
      String newEmail = (String)newEmailAddress.get();
      DUser user = userDao.findByEmail(newEmail); // Blocking
      if (null != user) {
        LOGGER.info("Email already taken {}", user.getEmail());
        throw new ConflictRestException("Email already taken");
      }

      try {
        user = futureUser.get(); // Blocking
      } catch (Exception e) {
        LOGGER.error("Failed to read async from datastore {}", e);
        throw new InternalServerErrorRestException("Failed to read async from datastore");
      }

      user.setEmail(newEmail);
      put(user);

      return true;
    } else {
      LOGGER.debug("No new email was found in cache or link expired");
      return false;
    }
  }

  @Override
  public void resetPassword(String email) {

    DUser user = getByEmail(email.toLowerCase()); // Throw 404 if not found

    Localization localization = localizationBuilderProvider.get().build();
    String subject = localization.getMessage("restPassword", "Reset password", user.getUsername());

    String token = tokenCache.generateTemporaryToken(tokenKey(user.getId(), TokenType.PASSWORD), 60 * 60 * 24); // token is valid 24

    String url = buildConfirmationURL(user.getId(), localization.getLocale(), token, "ResetPassword");
    LOGGER.debug("reset password url {}", url);

    String body = templateBuilderProvider.get()
        .templateName(resetPasswordTemplate)
        .put("confirmationUrl", url)
        .put("user", user)
        .put("baseUrl", uriInfoProvider.get().getBaseUri().toString())
        .build()
        .toString();

    emailService.sendEmail(user.getEmail(), user.getDisplayName(), subject, body, true);
  }

  @Override
  public boolean confirmResetPasswordUsingToken(Long userId, String newPassword, String token) {
    if (tokenCache.validateToken(tokenKey(userId, TokenType.PASSWORD), token)) {
      DUser user = getById(userId); // Will throw 404 if not found
      user.setPassword(passwordEncoder.encode(newPassword));
      put(user);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void changeUsername(Long userId, String newUsername) {

    if (shouldUseEmailAsUsername) {
      LOGGER.info("Using email as username, not allowed to change username");
      throw new BadRequestRestException("Using email as username, not allowed to change username");
    }

    Future<DUser> futureUser = getAsyncById(userId); // Non blocking

    // Check that username is unique
    DUser user = userDao.findByUsername(newUsername); // Blocking
    if (null != user) {
      LOGGER.info("Username already taken {}", user.getEmail());
      throw new ConflictRestException("Username already taken");
    }

    try {
      user = futureUser.get(); // Blocking
    } catch (Exception e) {
      LOGGER.error("Failed to read async from datastore {}", e);
      throw new InternalServerErrorRestException("Failed to read async from datastore");
    }

    user.setUsername(newUsername);
    put(user);

  }

  @Override
  public DUser createDefaultAdmin() {

    DUser admin = userDao.findByUsername(DEFAULT_ADMIN_USERNAME);
    if (null == admin) {
      admin = new DUser();
      admin.setDisplayName("Default admin (remove)");
      admin.setUsername(DEFAULT_ADMIN_USERNAME);
      admin.setPassword(passwordEncoder.encode("svinstia2014"));
      admin.setEmail("fake@email.com");
      admin.setState(DOAuth2User.ACTIVE_STATE);
      admin.setRoles(OAuth2UserResource.DEFAULT_ROLES_ADMIN);
      put(admin);
    }

    return admin;
  }

  private Long put(DUser user) {
    try {
      if (shouldUseEmailAsUsername) {
        // Keep username and email in synch
        user.setUsername(user.getEmail());
      }
      return userDao.put(user);
    } catch (IOException e) {
      LOGGER.error("Failed to save entity {}", e);
      throw new InternalServerErrorRestException("Failed to save user entity");
    }
  }

  @Override
  public DUser getById(Long id) {

    try {
      DUser dUser = userDao.get(id);
      if (null == dUser) {
        throw new NotFoundRestException(String.format("User not found %s", id));
      }
      return dUser;
    } catch (IOException e) {
      LOGGER.error("Failed to read user {}", id);
      throw new InternalServerErrorRestException(String.format("Failed to read user %s", id));
    }

  }

  private Future<DUser> getAsyncById(Long id) {
    try {
      return userDao.getAsync(null, id);
    } catch (IOException e) {
      LOGGER.error("Failed to read user {}", id);
      throw new InternalServerErrorRestException(String.format("Failed to read user %s", id));
    }
  }

  private DUser getByEmail(String email) {
    DUser user = userDao.findByEmail(email);
    if (null == user) {
      throw new NotFoundRestException();
    }
    return user;
  }

  @Override
  public void deleteById(Long id) {
    try {
      userDao.delete(id);
    } catch (IOException e) {
      LOGGER.error("Failed to delete user {}", e);
      throw new InternalServerErrorRestException(String.format("Failed to delete user"));
    }
  }

  @Override
  public CursorPage<DUser> readPage(int pageSize, String cursorKey) {
    return userDao.queryPage(pageSize, cursorKey);
  }


  @Override
  public CursorPage<DUser> getFriendsWith(Long id, int pageSize, String cursorKey) {
    return userDao.queryFriends(id, pageSize, cursorKey);
  }

  @Override
  public CursorPage<DUser> findMatchingUsersByEmail(String email, int pageSize, String cursorKey) {
    return userDao.queryByMatchingEmail(email.toLowerCase(), pageSize, cursorKey);
  }

  @Override
  public CursorPage<DUser> findMatchingUsersByUserName(String username, int pageSize, String cursorKey) {
    return userDao.queryByMatchingUsername(username, pageSize, cursorKey);
  }

  @Override
  public DUser update(Long id, DUser user, boolean isAdmin) {

    DUser existingUser = getById(id); // With raise 404 if not found

    // Only allow the user to update some properties
    existingUser.setAddress1(user.getAddress1());
    existingUser.setAddress2(user.getAddress2());
    existingUser.setBirthInfo(user.getBirthInfo());
    existingUser.setCity(user.getCity());
    existingUser.setCountry(user.getCountry());
    existingUser.setDisplayName(user.getDisplayName());
    existingUser.setEmail(user.getEmail().toLowerCase());
    existingUser.setFirstName(user.getFirstName());
    existingUser.setLastName(user.getLastName());
    existingUser.setPhoneNumber1(user.getPhoneNumber1());
    existingUser.setPhoneNumber2(user.getPhoneNumber2());
    existingUser.setZipCode(user.getZipCode());
    existingUser.setTimeZoneCanonicalId(user.getTimeZoneCanonicalId());
    if (null != user.getPreferredLanguage()) {
      existingUser.setPreferredLanguage(user.getPreferredLanguage());
    }

    if (isAdmin) {
      if (null != user.getState()) {
        existingUser.setState(user.getState());
      }
      if (null != user.getRoles()) {
        existingUser.setRoles(user.getRoles());
      }
    }

    put(existingUser);

    return existingUser;
  }

  @Override
  public void changePassword(Long id, String oldPassword, String newPassword) {

    DUser user = getById(id); // Will raise 404 if user is not found

    // Verify old password before setting new password
    if (passwordEncoder.matches(oldPassword, user.getPassword())) {
      user.setPassword(passwordEncoder.encode(newPassword));
      put(user);
    } else {
      throw new UnauthorizedRestException("Wrong password");
    }

  }

  @Override
  public DOAuth2User authenticate(String username, String password) {

    DUser user = userDao.findByUsername(username);
    if (null == user || user.getState() != DUser.ACTIVE_STATE) {
      LOGGER.debug("Username not found");
      return null;
    }

    return passwordEncoder.matches(password, user.getPassword()) ? user : null;
  }


  @Override
  public DOAuth2User createUser() {
    return new DUser();
  }

  @Override
  public DOAuth2User putUser(DOAuth2User user) {
    lowercaseEmail((DUser) user);
    put((DUser) user);
    return user;
  }

  @Override
  public DOAuth2User getUserById(Long id) {
    return getById(id);
  }

  @Inject(optional = true)
  public void setShouldVerifyAccountCreation(@Named("app.email.verification") boolean shouldVerifyAccountCreation) {
    this.shouldVerifyAccountCreation = shouldVerifyAccountCreation;
  }

  @Inject(optional = true)
  public void setUseEmailAsUsername(@Named("app.email.useAsUsername") boolean useEmailAsUsername) {
    this.shouldUseEmailAsUsername = useEmailAsUsername;
  }

  @Inject(optional = true)
  public void setVerifyAccountTemplate(@Named("app.template.verifyAccount") String verifyAccountTemplate) {
    this.verifyAccountTemplate = verifyAccountTemplate;
  }

  @Inject(optional = true)
  public void setResetPasswordTemplate(@Named("app.template.resetPassword") String resetPasswordTemplate) {
    this.resetPasswordTemplate = resetPasswordTemplate;
  }

  @Inject(optional = true)
  public void setVerifyEmaildTemplate(@Named("app.template.changeEmail") String changeEmailTemplate) {
    this.changeEmailTemplate = changeEmailTemplate;
  }

}
