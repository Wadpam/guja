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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class PropertyFileLocalizationTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileLocalization.class);

  private Localization localization;

  @Before
  public void setUp() throws Exception {

    localization = new PropertyFileLocalization(
        "i18n.TestBundle",
        new Locale.Builder()
            .setLanguage("en")
            .setRegion("US")
            .build(), new UTF8Control());

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
            .build(), new UTF8Control());

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

  @Test
  public void testSvLocalization() throws Exception {

    localization = new PropertyFileLocalization(
        "i18n.TestBundle",
        new Locale.Builder()
            .setLanguage("sv")
            .build(), new UTF8Control());

    assertTrue("SV value1".equals(localization.getMessage("key1", "default message")));

  }

  @Test
  public void testUTF8PropertyFiles() throws Exception {

    localization = new PropertyFileLocalization(
        "i18n.TestBundle",
        new Locale.Builder()
            .setLanguage("sv")
            .build(), new UTF8Control());

    String localizedMessage = localization.getMessage("specialChars", "failed");
    LOGGER.info("åäö", localizedMessage);
    assertTrue("åäö".equals(localizedMessage));

  }
}