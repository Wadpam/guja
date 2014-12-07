package com.wadpam.guja.domain;

import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Storing localized strings
 * @author mattiaslevin
 */
@Entity
public class Di18n extends AbstractLongEntity {

    @Basic
    private String key;

    @Basic
    private String baseBundle;

    @Basic
    private String locale;

    @Basic
    private String localizedMessage;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBaseBundle() {
        return baseBundle;
    }

    public void setBaseBundle(String baseBundle) {
        this.baseBundle = baseBundle;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }
}
