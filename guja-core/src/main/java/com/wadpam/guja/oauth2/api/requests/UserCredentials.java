package com.wadpam.guja.oauth2.api.requests;

/**
 * User credentials sent from the app.
 *
 * @author mattiaslevin
 */
public class UserCredentials extends ClientCredentials {

  public UserCredentials() {
  }

  private String username;
  private String password;
  private String grant_type;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getGrant_type() {
    return grant_type;
  }

  public void setGrant_type(String grant_type) {
    this.grant_type = grant_type;
  }

}
