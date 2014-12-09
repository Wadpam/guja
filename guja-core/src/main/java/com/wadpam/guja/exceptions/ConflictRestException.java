package com.wadpam.guja.exceptions;

import javax.ws.rs.core.Response;

/**
 * Map internal resource conflict with state to http 409.
 *
 * @author mattiaslevin
 */
public class ConflictRestException extends RestException {

  public ConflictRestException() {
    super(Response.Status.CONFLICT);
  }

  public ConflictRestException(String message) {
    super(Response.Status.CONFLICT, message);
  }
}
