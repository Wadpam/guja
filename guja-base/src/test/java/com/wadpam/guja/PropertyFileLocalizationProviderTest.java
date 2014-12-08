package com.wadpam.guja;

import com.wadpam.guja.i18n.Localization;
import com.wadpam.guja.i18n.PropertyFileLocalizationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class PropertyFileLocalizationProviderTest {

    private Localization localization;

    @Before
    public void setUp() throws Exception {

        localization = new PropertyFileLocalizationProvider(
                new Locale.Builder()
                        .setLanguage("en")
                        .setRegion("US")
                        .build(),
                "i18n.TestBundle").get();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testResourceBundleMissing() {

        localization = new PropertyFileLocalizationProvider(
                new Locale.Builder()
                        .setLanguage("en")
                        .setRegion("US")
                        .build(),
                "i18n.MissingBundle").get();

        assertTrue("default".equals(localization.getMessage("key1", "default")));

    }


    @Test
    public void testGetLocalization() {

        assertTrue("value1".equals(localization.getMessage("key1", "default message")));

    }

    @Test
    public void testGetLocalizationWithParameters() {

        assertTrue("param 1 param 2".equals(localization.getMessage("key3", "default %s %s", 1, 2)));

    }


    @Test
    public void testMissingLocalization() {

        assertTrue("default".equals(localization.getMessage("missingKey", "default")));

    }
}