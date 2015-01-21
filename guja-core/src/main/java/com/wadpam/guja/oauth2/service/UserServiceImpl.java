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
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.wadpam.guja.environment.ServerEnvironment;
import com.wadpam.guja.exceptions.ConflictRestException;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import com.wadpam.guja.exceptions.NotFoundRestException;
import com.wadpam.guja.exceptions.UnauthorizedRestException;
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

/**
 * User service implementation based on Mardao.
 *
 * @author mattiaslevin
 */
@Singleton
public class UserServiceImpl implements UserService, UserAuthenticationProvider, Oauth2UserProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final static String DEFAULT_ADMIN_USERNAME = "admin";

  private static final String VELOCITY_TEMPLATE_VERIFY_EMAIL = "email_verification.vm";
  private static final String VELOCITY_TEMPLATE_RESET_PASSWORD = "reset_password.vm";

  private DUserDaoBean userDao;

  private boolean shouldVerifyEmail = true;
  private String emailVerificationTemplate = VELOCITY_TEMPLATE_VERIFY_EMAIL;
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

    // TODO Consider only have a unique constraint on username (will have impact in other places)

    // Check if username already exists
    DUser existingUser = userDao.findByUsername(user.getUsername()); // Would be good if we can change to async request
    if (null != existingUser && existingUser.getState() == DUser.UNVERIFIED_STATE) {
      user.setId(existingUser.getId());
    } else if (null != existingUser) {
      LOGGER.info("Username already taken {}", user.getUsername());
      throw new ConflictRestException("Username already taken");
    }

    // Check if email already exists
    existingUser = userDao.findByEmail(user.getEmail()); // Would be good if we can change to async request
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

    Localization localization = localizationBuilderProvider.get().build();
    String subject = localization.getMessage("verifyEmailAddress", "Verify email address");
    String verifyUrl = createVerifyEmailUrl(user.getId(), localization.getLocale());
    LOGGER.debug("verify url {}", verifyUrl);

    String body = templateBuilderProvider.get()
        .templateName(emailVerificationTemplate)
        .put("verifyEmailLink", verifyUrl)
        .build()
        .toString();

    return emailService.sendEmail(user.getEmail(), user.getDisplayName(), subject, body, true);

  }

  private String createVerifyEmailUrl(Long userId, Locale locale) {

    String temporaryToken = tokenCache.generateTemporaryToken(userId.toString(), 30 * 60); // token is valid 30 minutes
    return uriInfoProvider.get().getBaseUriBuilder()
        .path("html")
        .path("EmailVerification")
        .path("emailVerification.html")
        .queryParam("id", userId)
        .queryParam("token", temporaryToken)
        .queryParam("language", locale.getLanguage())
        .build()
        .toString();
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

    Localization localization = localizationBuilderProvider.get().build();
    String subject = localization.getMessage("restPassword", "Reset password");
    String resetUrl = createResetPasswordUrl(user.getId(), localization.getLocale());

    String body = templateBuilderProvider.get()
        .templateName(resetPasswordTemplate)
        .put("resetPasswordLink", resetUrl)
        .build()
        .toString();

    emailService.sendEmail(user.getEmail(), user.getDisplayName(), subject, body, true);

  }

  private String createResetPasswordUrl(Long userId, Locale locale) {

    String temporaryToken = tokenCache.generateTemporaryToken(userId.toString(), 60 * 10); // token is valid 10 minutes
    return uriInfoProvider.get().getBaseUriBuilder()
        .path("html")
        .path("ResetPassword")
        .path("resetPassword.html")
        .queryParam("id", userId)
        .queryParam("token", temporaryToken)
        .queryParam("language", locale.getLanguage())
        .build()
        .toString();
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

  @Inject(optional = true)
  public void setShouldVerifyEmail(@Named("app.email.verification") boolean shouldVerifyEmail) {
    this.shouldVerifyEmail = shouldVerifyEmail;
  }

  @Inject(optional = true)
  public void setEmailVerificationTemplate(@Named("app.email.verify.template") String emailVerificationTemplate) {
    this.emailVerificationTemplate = emailVerificationTemplate;
  }

  @Inject(optional = true)
  public void setResetPasswordTemplate(@Named("app.password.template") String resetPasswordTemplate) {
    this.resetPasswordTemplate = resetPasswordTemplate;
  }
}
