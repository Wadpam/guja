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



import com.wadpam.guja.i18n.dao.Di18nDaoBean;

import java.util.Locale;

/**
 * Localization based on java resource bundle and property files.
 *
 * @author mattiaslevin
 */
public class DaoLocalization extends AbstractLocalization {

  private final Locale locale;
  private final String bundleName;
  private final Di18nDaoBean i18nDao;


  public DaoLocalization(String bundleName, Locale locale, Di18nDaoBean i18nDao) {
    this.locale = locale;
    this.bundleName = bundleName;
    this.i18nDao = i18nDao;
  }

  @Override
  public String getMessage(String key, String defaultMessage, Object... parameters) {

    String message = i18nDao.find(key, bundleName, locale.getLanguage());

    if (null == message) {
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
