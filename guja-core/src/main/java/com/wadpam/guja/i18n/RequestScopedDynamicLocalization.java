package com.wadpam.guja.i18n;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import com.wadpam.guja.i18n.dao.Di18nDaoBean;
import com.wadpam.guja.oauth2.provider.RequestScopedLocale;

/**
 * Request scoped localization using the locale in the current request.
 *
 * @author mattiaslevin
 */
@RequestScoped
public class RequestScopedDynamicLocalization extends DynamicLocalization{

  @Inject
  public RequestScopedDynamicLocalization(@Named("app.i18n.bundleName") String bundleName, // TODO Make optional and user a default as fallback
                                          Provider<RequestScopedLocale> localeProvider,
                                          Di18nDaoBean i18nDao) {
    super(bundleName, localeProvider.get().getLocale(), i18nDao);
  }

}
