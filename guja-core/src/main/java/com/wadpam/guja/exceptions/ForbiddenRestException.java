package com.wadpam.guja.exceptions;

import javax.ws.rs.core.Response;

/**
 * Forbidden (403)
 *
 * @author mattiaslevin
 */
public class ForbiddenRestException extends RestException {

  public ForbiddenRestException() {
    super(Response.Status.FORBIDDEN);
  }

  public ForbiddenRestException(String message) {
    super(Response.Status.FORBIDDEN, message);
  }

}
