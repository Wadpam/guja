package com.wadpam.guja.oauth2.api.requests;

/**
 * Refresh token request sent from the app.
 *
 * @author mattiaslevin
 */
public class RefreshTokenRequest extends ClientCredentials {

  public RefreshTokenRequest() {
  }

  private String refresh_token;
  private String grant_type;

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public String getGrant_type() {
    return grant_type;
  }

  public void setGrant_type(String grant_type) {
    this.grant_type = grant_type;
  }

}
