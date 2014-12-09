package com.wadpam.guja.exceptions;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Map to bad request, http 400
 *
 * @author mattiaslevin
 */
public class BadRequestRestException extends RestException {

  public BadRequestRestException() {
    super(Response.Status.BAD_REQUEST);
  }

  public BadRequestRestException(String message) {
    super(Response.Status.BAD_REQUEST, message);
  }

  public BadRequestRestException(Map<String, String> json) {
    super(Response.Status.BAD_REQUEST, json);
  }

}
