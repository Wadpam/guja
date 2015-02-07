package com.wadpam.guja.oauth2.provider;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Locale;

/**
 * Request scoped locale.
 *
 * @author mattiaslevin
 */
@RequestScoped
public class RequestScopedLocale {
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestScopedLocale.class);

  private final Locale locale;
  private final Locale defaultLocale;

  @Inject
  public RequestScopedLocale(HttpServletRequest request, @Named("app.locale.default") String language) {
    this.locale = request.getLocale();
    this.defaultLocale = new Locale.Builder().setLanguage(language).build();
    //LOGGER.debug("request locale {} {}", this.locale, this.locale.getLanguage());
  }

  public Locale getLocale() {
    return locale;
  }

  public Locale getDefaultLocale() {
    return defaultLocale;
  }

}
