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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.sun.jersey.spi.resource.Singleton;
import com.wadpam.guja.environment.ServerEnvironment;
import com.wadpam.guja.exceptions.ConflictRestException;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import com.wadpam.guja.exceptions.NotFoundRestException;
import com.wadpam.guja.exceptions.UnauthorizedRestException;
import com.wadpam.guja.i18n.RequestScopedPropertyFileLocalization;
import com.wadpam.guja.oauth2.api.OAuth2UserResource;
import com.wadpam.guja.oauth2.dao.DUserDaoBean;
import com.wadpam.guja.oauth2.domain.DOAuth2User;
import com.wadpam.guja.oauth2.domain.DUser;
import com.wadpam.guja.oauth2.provider.*;
import com.wadpam.guja.service.EmailService;
import com.wadpam.guja.template.RequestScopedVelocityTemplateStringWriterBuilder;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

/**
 * User service implementation based on Mardao.
 *
 * @author mattiaslevin
 */
@Singleton
public class UserServiceImpl implements UserService, UserAuthenticationProvider, Oauth2UserProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final static String DEFAULT_ADMIN_USERNAME = "admin";

  private static final String VELOCITY_TEMPLATE_VERIFY_EMAIL = "email_verification.vm"; // TODO Make a property
  private static final String VELOCITY_TEMPLATE_RESET_PASSWORD = "reset_password.vm"; // TODO Make a property

  private DUserDaoBean userDao;

  @Inject(optional = true)
  private boolean shouldVerifyEmail = true;

  private final PasswordEncoder passwordEncoder;
  private final ServerEnvironment severEnvironment;
  private final EmailService emailService;
  private final Provider<RequestScopedVelocityTemplateStringWriterBuilder> templateProvider;
  private final Provider<RequestScopedPropertyFileLocalization> localizationProvider;
  private final TemporaryTokenCache tokenCache;
  private final String baseUrl;

  @Inject
  public UserServiceImpl(DUserDaoBean userDao,
                         PasswordEncoder passwordEncoder,
                         EmailService emailService,
                         Provider<RequestScopedVelocityTemplateStringWriterBuilder> templateProvider,
                         Provider<RequestScopedPropertyFileLocalization> localizationProvider,
                         ServerEnvironment severEnvironment,
                         TemporaryTokenCache tokenCache,
                         @Named("app.baseUrl") String baseUrl) {

    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.templateProvider = templateProvider;
    this.localizationProvider = localizationProvider;
    this.severEnvironment = severEnvironment;
    this.tokenCache = tokenCache;
    this.baseUrl = baseUrl;

    if (severEnvironment.isDevEnvironment()) {
      createDefaultAdmin();
    }

  }

  @Override
  public DUser signup(DUser user) {

    // Check if username already exists
    if (null != userDao.findByUsername(user.getUsername())) { // TODO Change to asynch request
      LOGGER.info("Username already taken {}", user.getUsername());
      throw new ConflictRestException("Username already taken");
    }

    // Check if email already exists
    if (null != userDao.findByEmail(user.getEmail())) {  // TODO Change to asynch request
      LOGGER.info("Email already taken {}", user.getEmail());
      throw new ConflictRestException("Email already taken");
    }

    // encode password before storage
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    if (null == encodedPassword) {
      throw new InternalServerErrorRestException("Failed to encode user password");
    }
    user.setPassword(encodedPassword);

    // Lowercase email to enable search
    user.setEmail(user.getEmail().toLowerCase());

    user.setRoles(OAuth2UserResource.DEFAULT_ROLES_USER);

    // Is email validation enforced?
    user.setState((!shouldVerifyEmail || severEnvironment.isDevEnvironment())
        ? DUser.ACTIVE_STATE : DUser.UNVERIFIED_STATE);

    // Save
    put(user);

    if (shouldVerifyEmail) {
      // Send email confirmation email
      sendConfirmEmail(user);
    }

    return user;
  }

  private boolean sendConfirmEmail(DUser user) {

    String subject = localizationProvider.get().getMessage("verifyEmailAddress", "Verify email address"); // TODO provide translation
    String verifyUrl = createVerifyEmailUrl(user.getId(), localizationProvider.get().getLocale());

    String body = templateProvider.get()
        .templateName(VELOCITY_TEMPLATE_VERIFY_EMAIL)
        .put("verifyEmailLink", verifyUrl)
        .build()
        .toString();

    return emailService.sendEmail(user.getEmail(), user.getDisplayName(), subject, body, true);

  }

  private String createVerifyEmailUrl(Long userId, Locale locale) {

    String temporaryToken = tokenCache.generateTemporaryToken(userId.toString(), 30 * 60); // token is valid 30 minutes
    String pageUrl = String.format("%s/html/verify_email.html", baseUrl); // TODO he location of the web page must be project specific
    return String.format("%s?id=%s&token=%s&language=%s", pageUrl, userId, temporaryToken, locale.getLanguage());

  }

  @Override
  public boolean resendConfirmEmail(Long userId) {

    DUser user = getById(userId); // Will throw 404 if not found
    return sendConfirmEmail(user);

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

  private Long put(DUser dUser) {
    try {
      return userDao.put(dUser);
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

  public DUser getByEmail(String email) {
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
    existingUser.setEmail(user.getEmail().toLowerCase());
    existingUser.setDisplayName(user.getDisplayName());

    if (isAdmin) {
      existingUser.setState(user.getState());
      existingUser.setRoles(user.getRoles());
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
  public void resetPassword(String email) {

    DUser user = getByEmail(email); // Throw 404 if not found

    String subject = localizationProvider.get().getMessage("restPassword", "Reset password"); // TODO Provide translations
    String resetUrl = createResetPasswordUrl(user.getId(), localizationProvider.get().getLocale());

    String body = templateProvider.get()
        .templateName(VELOCITY_TEMPLATE_RESET_PASSWORD)
        .put("resetPasswordLink", resetUrl)
        .build()
        .toString();

    emailService.sendEmail(user.getEmail(), user.getDisplayName(), subject, body, true);

  }

  private String createResetPasswordUrl(Long userId, Locale locale) {

    String temporaryToken = tokenCache.generateTemporaryToken(userId.toString(), 60 * 10); // token is valid 10 minutes
    String pageUrl = String.format("%s/html/reset_password.html", baseUrl); // TODO he location of the web page must be project specific
    return String.format("%s?id=%s&token=%s&language=%s", pageUrl, userId, temporaryToken, locale.getLanguage());

  }

  @Override
  public boolean changePasswordUsingToken(Long userId, String newPassword, String token) {

    if (tokenCache.validateToken(userId.toString(), token)) {
      DUser user = getById(userId);
      user.setPassword(passwordEncoder.encode(newPassword));
      put(user);
      return true;
    }

    return false;
  }

  @Override
  public boolean confirmEmail(Long userId, String token) {
    if (tokenCache.validateToken(userId.toString(), token)) {
      DUser user = getById(userId);
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
  public DOAuth2User authenticate(String username, String password) {

    DUser user = userDao.findByUsername(username);
    if (null == user || user.getState() != DUser.ACTIVE_STATE) {
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
    put((DUser) user);
    return user;
  }

  @Override
  public DOAuth2User getUserById(Long id) {
    return getById(id);
  }
}
