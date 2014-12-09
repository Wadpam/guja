package com.wadpam.guja.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Base class for all rest exceptions.
 *
 * @author mattiaslevin
 */
public class RestException extends RuntimeException {
  private static final Logger LOGGER = LoggerFactory.getLogger(RestException.class);


  private Response.Status status;
  private Map<String, String> json;


  public RestException(Response.Status status) {
    this.status = status;
  }

  public RestException(Response.Status status, String message) {
    super(message);
    this.status = status;
  }

  public RestException(Response.Status status, Map<String, String> json) {
    this.status = status;
    this.json = json;
  }


  public Response.Status getStatus() {
    return status;
  }

  public Map<String, String> getJson() {
    return json;
  }

}
