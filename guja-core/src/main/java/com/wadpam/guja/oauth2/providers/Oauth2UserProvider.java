package com.wadpam.guja.oauth2.providers;

import com.wadpam.guja.oauth2.domain.DOAuth2User;

/**
 * Provide oauth2 compliant users.
 * @author mattiaslevin
 */
public interface Oauth2UserProvider {


    /**
     * Find a user by its id.
     * @param id unique user id
     * @return oauth2 compliant user
     */
    DOAuth2User getUserById(Long id);

    /**
     * Create a new Oauth2 user.
     * @return
     */
    DOAuth2User createUser();

    /**
     * Update user.
     */
    DOAuth2User putUser(DOAuth2User user);

}
