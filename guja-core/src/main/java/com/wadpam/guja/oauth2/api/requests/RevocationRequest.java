package com.wadpam.guja.oauth2.api.requests;

/**
 * Revocation request send from the app.
 *
 * @author mattiaslevin
 */
public class RevocationRequest extends ClientCredentials {

  private String token;
  private String token_type_hint;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getToken_type_hint() {
    return token_type_hint;
  }

  public void setToken_type_hint(String token_type_hint) {
    this.token_type_hint = token_type_hint;
  }
}
