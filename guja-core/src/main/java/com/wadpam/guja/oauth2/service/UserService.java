package com.wadpam.guja.oauth2.service;

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
   * Create a default admin account.
   * This account should be deleted in productions environment after the system has been configured.
   *
   * @return
   */
  DUser createDefaultAdmin();
}
