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
   * Create a default admin in the datastore.
   */
  @Override
  public Object processTask(String taskName, Map<String, String[]> parameterMap) {

    if ("createAdmin".equalsIgnoreCase(taskName)) {
      return userService.createDefaultAdmin();
    }

    return null;

  }

}
