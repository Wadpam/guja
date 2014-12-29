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

import com.google.inject.Provider;
import com.wadpam.guja.i18n.Localization;
import com.wadpam.guja.i18n.PropertyFileLocalization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class PropertyFileLocalizationProviderTest {

  private Localization localization;

  @Before
  public void setUp() throws Exception {

    localization = new PropertyFileLocalization(
        "i18n.TestBundle",
        new Locale.Builder()
            .setLanguage("en")
            .setRegion("US")
            .build());

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testResourceBundleMissing() {

    localization = new PropertyFileLocalization(
        "i18n.MissingBundle",
        new Locale.Builder()
            .setLanguage("en")
            .setRegion("US")
            .build());

    assertTrue("default message".equals(localization.getMessage("key1", "default message")));

  }


  @Test
  public void testGetLocalization() {

    assertTrue("value1".equals(localization.getMessage("key1", "default message")));

  }

  @Test
  public void testGetLocalizationWithParameters() {

    assertTrue("param 1 param 2".equals(localization.getMessage("key3", "default message", 1, 2)));

  }


  @Test
  public void testMissingLocalization() {

    assertTrue("default message".equals(localization.getMessage("missingKey", "default message")));

  }
}