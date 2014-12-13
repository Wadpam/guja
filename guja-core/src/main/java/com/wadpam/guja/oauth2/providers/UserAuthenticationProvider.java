package com.wadpam.guja.oauth2.providers;

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

import com.wadpam.guja.oauth2.domain.DOAuth2User;

/**
 * @author mattiaslevin
 */
public interface UserAuthenticationProvider {

  /**
   * Authenticate a user during sign in.
   *
   * @param username unique user name
   * @param password password
   * @return a oauth2 user if authentication was successful, otherwise null
   */
  // TODO Do not like the return value, refactor later on
  DOAuth2User authenticate(String username, String password);

}
