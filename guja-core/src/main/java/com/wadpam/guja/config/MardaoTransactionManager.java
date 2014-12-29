package com.wadpam.guja.config;

/*
 * #%L
 * guja-core
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.dao.TransFunc;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom Date: 1/21/14 Time: 8:59 PM
 */
public class MardaoTransactionManager implements MethodInterceptor {
  static final Logger LOGGER = LoggerFactory.getLogger(MardaoTransactionManager.class);

  private final Provider<Supplier> supplier;

  @Inject
  public MardaoTransactionManager(Provider<Supplier> supplier) {
    this.supplier = supplier;
  }

  /**
   * Implement this method to perform extra treatments before and after the invocation. Polite implementations would certainly like
   * to invoke {@link org.aopalliance.intercept.Joinpoint#proceed()}.
   *
   * @param invocation the method invocation joinpoint
   * @return the result of the call to {@link org.aopalliance.intercept.Joinpoint#proceed()}, might be intercepted by the
   * interceptor.
   * @throws Throwable if the interceptors or the target-object throws an exception.
   */
  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    final Method method = invocation.getMethod();
    final Transactional annotation = method.getAnnotation(Transactional.class);

    Object result;
    if (null == annotation) {
      result = invocation.proceed();
    } else {
      result = AbstractDao.withTransaction(new TransFunc<Object>() {
        @Override
        public Object apply() throws IOException {
          try {
            return invocation.proceed();
          } catch (Throwable throwable) {
            throw new IOException("withTransaction invocation.proceed()", throwable);
          }
        }
      }, true, supplier.get());
    }

    return result;
  }
}
