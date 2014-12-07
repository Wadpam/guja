package com.wadpam.guja.oauth2.providers;


/**
 * Strategy for generating access tokens.
 * @author mattiasLevin
 */
public interface AccessTokenGenerator {

    /**
     * Generate a new access_token
     * @return
     */
    String generate();

}
