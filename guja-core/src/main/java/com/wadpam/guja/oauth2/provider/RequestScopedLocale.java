package com.wadpam.guja.oauth2.provider;

import com.google.inject.Inject;
import com.google.inject.name.Named;
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
  private final Locale defaultLocale;

  @Inject
  public RequestScopedLocale(HttpServletRequest request, @Named("app.locale.default") String language) {
    locale = request.getLocale();
    defaultLocale = new Locale.Builder().setLanguage(language).build();
  }

  public Locale getLocale() {
    return locale;
  }

  public Locale getDefaultLocale() {
    return defaultLocale;
  }

}
