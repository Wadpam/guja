package com.wadpam.guja.i18n;

import com.google.inject.Inject;
import com.wadpam.guja.dao.Di18nDaoBean;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Request build localization provider.
 * @author mattiaslevin
 */
public class PropertyFileLocalizationProvider implements LocalizationProvider {

    private final Locale locale;

    @Inject
    public PropertyFileLocalizationProvider(@Context HttpServletRequest request) {
        this.locale = request.getLocale();
    }

    public Localization getLocalization(String bundleName) {

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(checkNotNull(bundleName),
                checkNotNull(locale),
                ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));

        return new Localization() {
            @Override
            public String getMessage(String key, String defaultMessage, Object... parameters) {

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
                return resourceBundle.getLocale();
            }
        };

    }

}
