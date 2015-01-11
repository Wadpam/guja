package com.wadpam.guja.i18n;

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;

import java.lang.reflect.Method;

/**
 * Base implementation of localization implementations.
 *
 * @author mattiaslevin
 */
public abstract class AbstractLocalization implements Localization {

  @Override
  public <T extends Localizable> T getLocalizable(Class<T> clazz) {
    return Reflection.newProxy(clazz, new AbstractInvocationHandler() {
      @Override
      protected String handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        return getMessage(method.getName(), method.getName(), args);
      }
    });
  }

}
