package com.wadpam.guja.oauth2.providers;

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
