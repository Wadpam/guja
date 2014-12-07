package com.wadpam.guja.i18n;

import com.google.inject.Inject;
import com.wadpam.guja.dao.Di18nDaoBean;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Locale;

/**
 * Request build localization provider.
 * @author mattiaslevin
 */
public class DynamicLocalizationProvider implements LocalizationProvider {

    private final Locale locale;
    private final Di18nDaoBean i18nDao;

    @Inject
    public DynamicLocalizationProvider(@Context HttpServletRequest request, Di18nDaoBean i18nDao) {
        this.locale = request.getLocale();
        this.i18nDao = i18nDao;
    }

    @Override
    public Localization getLocalization(final String bundleName) {

        return new Localization() {
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
        };

    }

}
