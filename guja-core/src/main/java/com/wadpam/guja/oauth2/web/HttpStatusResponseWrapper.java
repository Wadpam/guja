package com.wadpam.guja.oauth2.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Simple http servlet response wrapper for exposing the http status code.
 *
 * @author mattiaslevin
 */
public class HttpStatusResponseWrapper extends HttpServletResponseWrapper {

  private int httpStatus;

  public HttpStatusResponseWrapper(HttpServletResponse response) {
    super(response);
  }

  @Override
  public void sendError(int sc) throws IOException {
    httpStatus = sc;
    super.sendError(sc);
  }

  @Override
  public void sendError(int sc, String msg) throws IOException {
    httpStatus = sc;
    super.sendError(sc, msg);
  }


  @Override
  public void setStatus(int sc) {
    httpStatus = sc;
    super.setStatus(sc);
  }

  public int getStatus() {
    return httpStatus;
  }

}
