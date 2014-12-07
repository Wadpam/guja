package com.wadpam.guja.oauth2.providers;


/**
 * Password encoder strategy.
 * @author mattiaslevin
 */
public interface PasswordEncoder {

    /**
     * Encode a clear text rawPassword before storage.
     * @param rawPassword raw password provided by the user when signing up
     * @return encoded password
     */
    String encode(String rawPassword);


    /**
     * Check if a raw password matches a saved encoded password.
     * @param rawPassword the raw password provided by the user when authenticating
     * @param encodedPassword the stored encoded password
     * @return true of the passwords match, otherwise false
     */
    boolean matches(String rawPassword, String encodedPassword);

}
