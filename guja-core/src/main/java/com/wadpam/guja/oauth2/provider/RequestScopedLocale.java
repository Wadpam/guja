package com.wadpam.guja.oauth2.provider;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Request scoped locale.
 *
 * @author mattiaslevin
 */
@RequestScoped
public class RequestScopedLocale {

  private final Locale locale;

  @Inject
  public RequestScopedLocale(HttpServletRequest request) {
    locale = request.getLocale();
  }

  public Locale getLocale() {
    return locale;
  }

}
