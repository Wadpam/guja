package com.wadpam.guja.i18n;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import com.wadpam.guja.dao.Di18nDaoBean;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Locale;

/**
 * Request build localization provider.
 * @author mattiaslevin
 */
@RequestScoped
public class DynamicLocalizationProvider implements Provider<Localization> {

    private final Locale locale;
    private final String bundleName;
    private final Di18nDaoBean i18nDao;

    @Inject
    public DynamicLocalizationProvider(@Named("app.i18n.bundleName") String bundleName,
                                       @Context HttpServletRequest request,
                                       Di18nDaoBean i18nDao) {
        this(request.getLocale(), bundleName, i18nDao);
    }

    public DynamicLocalizationProvider(Locale locale, String bundleName, Di18nDaoBean i18nDao) {
        this.locale = locale;
        this.bundleName = bundleName;
        this.i18nDao = i18nDao;
    }

    @Override
    public Localization get() {

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
