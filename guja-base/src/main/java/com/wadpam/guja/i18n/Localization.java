package com.wadpam.guja.i18n;

import java.util.Locale;

/**
 * @author mattiaslevin
 */
public interface Localization {

  /**
   * Get a localized message for a given key.
   *
   * @param key            key
   * @param defaultMessage default message will be used if no localized message is found.
   * @param parameters     Optional. Parameters
   * @return a translated string
   */
  String getMessage(String key, String defaultMessage, Object... parameters);

  /**
   * Get the locale used by the localization.
   */
  Locale getLocale();

}
