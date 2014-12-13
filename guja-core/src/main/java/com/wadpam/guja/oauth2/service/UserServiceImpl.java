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
import com.sun.jersey.spi.resource.Singleton;
import com.wadpam.guja.exceptions.ConflictRestException;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import com.wadpam.guja.exceptions.NotFoundRestException;
import com.wadpam.guja.exceptions.UnauthorizedRestException;
import com.wadpam.guja.oauth2.api.OAuth2UserResource;
import com.wadpam.guja.oauth2.dao.DUserDaoBean;
import com.wadpam.guja.oauth2.domain.DOAuth2User;
import com.wadpam.guja.oauth2.domain.DUser;
import com.wadpam.guja.oauth2.providers.Oauth2UserProvider;
import com.wadpam.guja.oauth2.providers.PasswordEncoder;
import com.wadpam.guja.oauth2.providers.ServerEnvironmentProvider;
import com.wadpam.guja.oauth2.providers.UserAuthenticationProvider;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User service implementation based on Mardao.
 *
 * @author mattiaslevin
 */
@Singleton
public class UserServiceImpl implements UserService, UserAuthenticationProvider, Oauth2UserProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final static String DEFAULT_ADMIN_USERNAME = "admin";

  private DUserDaoBean userDao;

  @Inject(optional = true)
  private boolean shouldVerifyEmail = true;

  private ServerEnvironmentProvider severEnvironment;
  private PasswordEncoder passwordEncoder;


  @Inject
  public UserServiceImpl(DUserDaoBean userDao, PasswordEncoder passwordEncoder, ServerEnvironmentProvider severEnvironment) {
    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
    this.severEnvironment = severEnvironment;

    if (severEnvironment.isDevEnvironment()) {
      createDefaultAdmin();
    }

  }

  @Override
  public DUser signup(DUser user) {

    // Check if username already exists
    if (null != userDao.findByUsername(user.getUsername())) {
      LOGGER.info("Username already taken {}", user.getUsername());
      throw new ConflictRestException("Username already taken");
    }

    // encode password before storage
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    if (null == encodedPassword) {
      throw new InternalServerErrorRestException("Failed to encode user password");
    }
    user.setPassword(encodedPassword);

    user.setRoles(OAuth2UserResource.DEFAULT_ROLES_USER);

    // Is email validation enforced?
    user.setState((!shouldVerifyEmail || severEnvironment.isDevEnvironment())
        ? DUser.ACTIVE_STATE : DUser.UNVERIFIED_STATE);

    // Save
    put(user);

    if (shouldVerifyEmail && !severEnvironment.isDevEnvironment()) {

      // TODO Send out verification email

    }

    return user;
  }

  @Override
  public DUser createDefaultAdmin() {

    // Only create the default admin if no users already exists
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
  public CursorPage<DUser> readPage(String cursorKey, int pageSize) {
    return userDao.queryPage(pageSize, cursorKey);
  }

  @Override
  public DUser update(Long id, DUser user, boolean isAdmin) {

    DUser existingUser = getById(id); // With raise 404 if not found

    // Only allow the user to update some properties
    existingUser.setEmail(user.getEmail());
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

    DUser dUser = getById(id); // Will raise 404 if user is not found

    // Verify old password before setting new password
    if (passwordEncoder.matches(oldPassword, dUser.getPassword())) {
      dUser.setPassword(passwordEncoder.encode(newPassword));
    } else {
      throw new UnauthorizedRestException("Wrong password");
    }

  }

  public DOAuth2User authenticate(String username, String password) {

    DUser user = userDao.findByUsername(username);
    if (null == user || user.getState() != DUser.ACTIVE_STATE) {
      return null;
    }

    return passwordEncoder.matches(password, user.getPassword()) ? user : null;
  }

  // TODO Missing implementation


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
