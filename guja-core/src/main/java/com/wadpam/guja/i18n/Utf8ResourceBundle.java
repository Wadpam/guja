package com.wadpam.guja.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Create a UTF-8 friendly resource bundle.
 *
 * Take no credit, copied of internet.
 */
public class Utf8ResourceBundle {

  public static final ResourceBundle getBundle(final String baseName, final Locale locale) {
    return createUtf8PropertyResourceBundle(ResourceBundle.getBundle(baseName, locale));
  }

  private static ResourceBundle createUtf8PropertyResourceBundle(final ResourceBundle bundle) {
    if (!(bundle instanceof PropertyResourceBundle)) {
      return bundle;
    }
    return new Utf8PropertyResourceBundle((PropertyResourceBundle) bundle);
  }

  private static class Utf8PropertyResourceBundle extends ResourceBundle {

    private final PropertyResourceBundle bundle;

    private Utf8PropertyResourceBundle(final PropertyResourceBundle bundle) {
      this.bundle = bundle;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration getKeys() {
      return bundle.getKeys();
    }

    @Override
    protected Object handleGetObject(final String key) {
      final String value = bundle.getString(key);
      if (null == value) {
        return null;
      }
      try {
        return new String(value.getBytes("ISO-8859-1"), "UTF-8");
      } catch (final UnsupportedEncodingException e) {
        throw new RuntimeException("Encoding not supported", e);
      }
    }
  }

}
