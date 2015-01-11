package com.wadpam.guja.i18n;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.wadpam.guja.i18n.dao.Di18nDaoBean;
import com.wadpam.guja.oauth2.provider.RequestScopedLocale;

import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Dao based localization builder.
 *
 * @author mattiaslevin
 */
public class DaoLocalizationBuilder {

  final private Provider<RequestScopedLocale> localeProvider;
  final private Di18nDaoBean dao;

  private String bundleName;
  private Locale locale;

  @Inject
  public DaoLocalizationBuilder(Provider<RequestScopedLocale> localeProvider, Di18nDaoBean doa) {
    this.localeProvider = localeProvider;
    this.dao = doa;
  }

  public DaoLocalizationBuilder bundleName(String bundleName) {
    this.bundleName = bundleName;
    return this;
  }

  public DaoLocalizationBuilder locale(Locale locale) {
    this.locale = locale;
    return this;
  }

  public Localization build() {
    checkNotNull(bundleName);
    return new DaoLocalization(bundleName, null != locale ? locale : localeProvider.get().getLocale(), dao);
  }

  @Inject(optional = true)
  public void setBundleName(@Named("app.i18n.bundleName") String bundleName) {
    this.bundleName = bundleName;
  }

}
