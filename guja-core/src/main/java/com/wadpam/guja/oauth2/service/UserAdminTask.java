package com.wadpam.guja.oauth2.service;

import com.google.inject.Inject;
import com.wadpam.guja.admintask.AdminTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * User admin tasks.
 *
 * @author mattiaslevin
 */

public class UserAdminTask implements AdminTask {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserAdminTask.class);


  private final UserService userService;


  @Inject
  public UserAdminTask(UserService userService) {
    this.userService = userService;
  }


  /**
   * Create a default admin.
   * The admin will only be created of no users exist in the datastore.
   */
  @Override
  public Object processTask(String taskName, Map<String, String[]> parameterMap) {

    if ("createAdmin".equalsIgnoreCase(taskName)) {
      return userService.createDefaultAdmin();
    }

    return null;

  }

}
