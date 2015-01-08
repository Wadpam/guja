package com.wadpam.guja.oauth2.provider;

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
 * Provide oauth2 compliant users.
 *
 * @author mattiaslevin
 */
public interface Oauth2UserProvider {


  /**
   * Find a user by its id.
   *
   * @param id unique user id
   * @return oauth2 compliant user
   */
  DOAuth2User getUserById(Long id);

  /**
   * Create a new Oauth2 user.
   *
   * @return
   */
  DOAuth2User createUser();

  /**
   * Update user.
   */
  DOAuth2User putUser(DOAuth2User user);

}
