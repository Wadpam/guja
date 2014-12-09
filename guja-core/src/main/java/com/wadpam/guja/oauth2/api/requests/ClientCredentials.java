package com.wadpam.guja.oauth2.api.requests;

/**
 * Base class for oauth related requests from the app.
 *
 * @author mattiaslevin
 */
public class ClientCredentials {

  public ClientCredentials() {
  }

  private String client_id;
  private String client_secret;

  @Override
  public String toString() {
    return String.format("id %s, secret %s", client_id, client_secret);
  }

  public String getClient_id() {
    return client_id;
  }

  public void setClient_id(String client_id) {
    this.client_id = client_id;
  }

  public String getClient_secret() {
    return client_secret;
  }

  public void setClient_secret(String client_secret) {
    this.client_secret = client_secret;
  }
}
