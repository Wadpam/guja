package com.wadpam.guja.i18n;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.wadpam.guja.oauth2.provider.RequestScopedLocale;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author mattiaslevin
 */
public class PropertyFileLocalizationBuilder {

  final private Provider<RequestScopedLocale> localeProvider;

  private String bundleName;
  private Locale locale;

  @Inject
  public PropertyFileLocalizationBuilder(Provider<RequestScopedLocale> localeProvider) {
    this.localeProvider = localeProvider;
  }

  public PropertyFileLocalizationBuilder bundleName(String bundleName) {
    this.bundleName = bundleName;
    return this;
  }

  public PropertyFileLocalizationBuilder locale(Locale locale) {
    this.locale = locale;
    return this;
  }

  public PropertyFileLocalization build() {
    checkNotNull(bundleName);
    return new PropertyFileLocalization(bundleName, null != locale ? locale : localeProvider.get().getLocale());
  }

  @Inject(optional = true)
  public void setBundleName(@Named("app.i18n.bundleName") String bundleName) {
    this.bundleName = bundleName;
  }

}
