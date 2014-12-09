package com.wadpam.guja.i18n;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
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
@RequestScoped
public class PropertyFileLocalizationProvider implements Provider<Localization> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileLocalizationProvider.class);


    private ResourceBundle resourceBundle;


    @Inject
    public PropertyFileLocalizationProvider(@Named("app.i18n.bundleName") String bundleName,
                                            @Context HttpServletRequest request) {
        this(bundleName, request.getLocale());
    }

    public PropertyFileLocalizationProvider(String bundleName, Locale locale) {
        try {
            resourceBundle = ResourceBundle.getBundle(checkNotNull(bundleName),
                    checkNotNull(locale),
                    ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
        } catch (MissingResourceException e) {
            LOGGER.warn("Resource bundle {} not found", bundleName);
        }
    }

    @Override
    public Localization get() {

        return new Localization() {

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
                return resourceBundle.getLocale();
            }
        };

    }

}
