package com.wadpam.guja.i18n;

/*
 * #%L
 * guja-base
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Request build localization provider.
 *
 * @author mattiaslevin
 */
public class PropertyFileLocalization implements Localization {
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileLocalization.class);


  private ResourceBundle resourceBundle;
  private final Locale locale;


  public PropertyFileLocalization(String bundleName, Locale locale) {
    this.locale = locale;

    try {
      resourceBundle = ResourceBundle.getBundle(checkNotNull(bundleName),
          checkNotNull(locale),
          ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    } catch (MissingResourceException e) {
      LOGGER.warn("Resource bundle {} not found", bundleName);
      resourceBundle = null;
    }

  }


  @Override
  public String getMessage(String key, String defaultMessage, Object... parameters) {

    if (null == resourceBundle) {
      return defaultMessage;
    }

    String message;
    try {
      message = resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      message = defaultMessage;
    }

    if (parameters.length > 0) {
      message = String.format(message, parameters);
    }

    return message;
  }

  @Override
  public Locale getLocale() {
    return locale;
  }

}