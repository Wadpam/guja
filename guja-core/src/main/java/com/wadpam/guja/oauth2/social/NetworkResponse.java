/*
 * INSERT COPYRIGHT HERE
 */

package com.wadpam.guja.oauth2.social;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sosandstrom
 */
public class NetworkResponse<J> {

  private J body;
  private final int code;
  private final Map<String, List<String>> headers;
  private final String message;

  public NetworkResponse(int code, Map<String, List<String>> headers, String message) {
    this.code = code;
    this.headers = headers;
    this.message = message;
  }

  public J getBody() {
    return body;
  }

  public void setBody(J body) {
    this.body = body;
  }

  public int getCode() {
    return code;
  }

  public String getHeader(String name) {
    if (null == headers) {
      return null;
    }

    List<String> values = headers.get(name);
    if (null == values || values.isEmpty()) {
      return null;
    }

    return values.get(0);
  }

  public Set<String> getHeaderNames() {
    return headers.keySet();
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public String getMessage() {
    return message;
  }

}
