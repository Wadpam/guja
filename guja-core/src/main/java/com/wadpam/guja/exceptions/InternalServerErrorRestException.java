package com.wadpam.guja.exceptions;

import javax.ws.rs.core.Response;

/**
 * Map exception to http 500
 *
 * @author mattiaslevin
 */
public class InternalServerErrorRestException extends RestException {

  public InternalServerErrorRestException() {
    super(Response.Status.INTERNAL_SERVER_ERROR);
  }

  public InternalServerErrorRestException(String message) {
    super(Response.Status.INTERNAL_SERVER_ERROR, message);
  }
}
