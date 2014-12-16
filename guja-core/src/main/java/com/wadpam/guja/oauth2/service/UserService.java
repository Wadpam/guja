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

import com.wadpam.guja.oauth2.domain.DUser;
import net.sf.mardao.core.CursorPage;

/**
 * User service.
 *
 * @author mattiaslevin
 */
public interface UserService {


  /**
   * Signup new user.
   * The username must be unique or http 409 (Conflict) will be returned.
   *
   * @param user User details
   * @return A new user domain object
   */
  DUser signup(DUser user);

  /**
   * Get user by user id.
   *
   * @param id unique user id
   * @return user domain object
   */
  DUser getById(Long id);

  /**
   * Delete user.
   *
   * @param id unique user id
   */
  void deleteById(Long id);

  /**
   * Get a page of users.
   *
   * @param cursorKey Optional. Cursor key.
   * @param pageSize  Optional. Page size
   * @return a page of user domain objects
   */
  CursorPage<DUser> readPage(String cursorKey, int pageSize);

  /**
   * Update a user.
   *
   * @param id      unique user id
   * @param user    new user info
   * @param isAdmin set to true if the current user has admin rights
   * @return update user domain object
   */
  DUser update(Long id, DUser user, boolean isAdmin);

  /**
   * Change a users password.
   *
   * @param id          unique user id
   * @param oldPassword old password
   * @param newPassword new password
   */
  void changePassword(Long id, String oldPassword, String newPassword);

  /**
   * Reset a users password by sending an password reset email.
   *
   * @param email unique user email
   */
  void resetPassword(String email);

  /**
   * Create a default admin account.
   * This account should be deleted in productions environment after the system has been configured.
   *
   * @return the create admin user
   */
  DUser createDefaultAdmin();


  /**
   * Get users that added me to their friends list.
   * @param id current user
   * @param cursorKey Optional. Cursor key
   * @param pageSize Optional. Page size
   * @return page of users
   */
  CursorPage<DUser> getFriendsWith(Long id, String cursorKey, int pageSize);

}
