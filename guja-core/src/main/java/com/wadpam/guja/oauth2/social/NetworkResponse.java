/*
 * INSERT COPYRIGHT HERE
 */

package com.wadpam.guja.oauth2.social;

/*
 * #%L
 * guja-core
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
