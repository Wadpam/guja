package com.wadpam.guja.guice;

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
