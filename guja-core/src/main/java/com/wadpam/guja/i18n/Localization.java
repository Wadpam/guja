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

import java.util.Locale;

/**
 * Localization interface.
 *
 * @author mattiaslevin
 */
public interface Localization {

  /**
   * Get a localizable instance of a user defined localizable interface.
   * @param clazz
   * @return
   */
  <T extends Localizable> T getLocalizable(Class<T> clazz);

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
