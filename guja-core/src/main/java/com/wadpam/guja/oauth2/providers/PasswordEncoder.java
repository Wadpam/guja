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


/**
 * Password encoder strategy.
 *
 * @author mattiaslevin
 */
public interface PasswordEncoder {

  /**
   * Encode a clear text rawPassword before storage.
   *
   * @param rawPassword raw password provided by the user when signing up
   * @return encoded password
   */
  String encode(String rawPassword);


  /**
   * Check if a raw password matches a saved encoded password.
   *
   * @param rawPassword     the raw password provided by the user when authenticating
   * @param encodedPassword the stored encoded password
   * @return true of the passwords match, otherwise false
   */
  boolean matches(String rawPassword, String encodedPassword);

}
