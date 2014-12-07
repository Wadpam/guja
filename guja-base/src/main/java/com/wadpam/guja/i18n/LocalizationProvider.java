package com.wadpam.guja.i18n;

/**
 * Localization provider interface.
 * @author mattiaslevin
 */
public interface LocalizationProvider {

    /**
     * Get localization for a specific bundle.
     * @param bundleName unique bundle id
     * @return Localization that can be used for translation
     */
    Localization getLocalization(String bundleName);

}
