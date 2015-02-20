package com.wadpam.guja.i18n;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class LocalizableTest {

  Localization localization;

  @Before
  public void setUp() throws Exception {

    localization = new PropertyFileLocalization(
        "i18n.TestBundle2",
        new Locale.Builder()
            .setLanguage("en")
            .setRegion("US")
            .build(), new UTF8Control());
  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testTranslation() throws Exception {

    Localized localized = localization.getLocalizable(Localized.class);

    assertTrue(localized.ok().equals("OK"));
    assertTrue(localized.tryAgain().equals("Please try again!"));
    assertTrue(localized.missing().equals("missing"));

  }
}