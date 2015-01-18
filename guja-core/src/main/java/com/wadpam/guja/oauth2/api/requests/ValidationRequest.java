package com.wadpam.guja.oauth2.api.requests;

/**
 * A request to validate an access token.
 *
 * @author mattiaslevin
 */
public class ValidationRequest extends ClientCredentials {

  private String access_token;

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }
}
