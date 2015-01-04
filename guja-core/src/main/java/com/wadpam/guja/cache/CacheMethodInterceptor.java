package com.wadpam.guja.cache;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Cached;
import net.sf.mardao.dao.CrudDao;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Method interceptor for adding caching to core crud methods.
 *
 * @author sosandstrom
 * @author mattiaslevin
 */
public class CacheMethodInterceptor implements MethodInterceptor {
  static final Logger LOGGER = LoggerFactory.getLogger(CacheMethodInterceptor.class);

  private final Map<String, Cache<? extends Serializable, ?>> namespaces = new HashMap<>();

  private final Provider<CacheBuilder> cacheBuilderProvider;
  public CacheMethodInterceptor(Provider<CacheBuilder> cacheBuilderProvider) {
    this.cacheBuilderProvider = cacheBuilderProvider;
  }

  private static final String GET_METHOD_NAME = "get";
  private static final String PUT_METHOD_NAME = "put";
  private static final String DELETE_METHOD_NAME = "delete";

  static Method readMethod, putMethod, deleteMethod, pageMethod;

  static {
    try {
      // TODO read, put, delete does not work with equals, not sure why
      //readMethod = CrudDao.class.getMethod("get", Serializable.class);
      //putMethod = CrudDao.class.getMethod("put", Object.class);
      //deleteMethod = CrudDao.class.getMethod("delete", Serializable.class);
      pageMethod = CrudDao.class.getMethod("queryPage", int.class, String.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {

    final Class clazz = getClass(invocation);
    Cache cache = getCacheInstance(clazz);

    final Object[] args = invocation.getArguments();
    final Method method = invocation.getMethod();
    LOGGER.info("invoking on {}, isAnnotated {}", method, method.isAnnotationPresent(Cached.class));

    if (GET_METHOD_NAME.equals(method.getName())) {
      LOGGER.info("   get");

      final Object id = invocation.getArguments()[0];
      checkNotNull(id);
      final Optional<?> optionalEntity = (Optional<?>)cache.get(id, new Callable() {
        @Override
        public Object call() throws Exception {
          try {
            final Object entity = invocation.proceed();
            return null != entity ? Optional.of(entity) : Optional.absent();
          } catch (Throwable throwable) {
            LOGGER.error("Failed to populate cache {} {}", clazz.getName(), throwable);
            return null;
          }
        }
      });
      return null != optionalEntity && optionalEntity.isPresent() ? optionalEntity.get() : null;

    }
    else if (PUT_METHOD_NAME.equals(method.getName())) {
      LOGGER.info("   put");

      final Object id = invocation.proceed();
      final Object entity = invocation.getArguments()[0];
      checkNotNull(id);
      checkNotNull(entity);
      cache.put(id, Optional.of(entity));
      return id;

    }
    else if (DELETE_METHOD_NAME.equals(method.getName())) {
      LOGGER.info("   delete");

      invocation.proceed();
      final Object id = invocation.getArguments()[0];
      checkNotNull(id);
      cache.invalidate(id);
      return null;

    }
    else if (pageMethod.equals(method)) {
      LOGGER.info("   page");

      Integer pageSize = (Integer) args[0];
      String cursorString = (String) args[1];

      // TODO Missing implementation

      return null;

    } else {
      // Let everything else pass through unmodified
      return invocation.proceed();
    }

  }

  private Cache getCacheInstance(Class clazz) {
    final String className = clazz.getName();

    Cache cache = namespaces.get(className);
    if (null == cache) {
      final Cached annotation = (Cached) clazz.getAnnotation(Cached.class);
      LOGGER.debug("Build new dao cache for {}", className);
      CacheBuilder<? extends Serializable, Optional<?>> cacheBuilder = cacheBuilderProvider.get();
      if (null != annotation.from() || "".equals(annotation.from())) {
        cacheBuilder.from(annotation.from());
      } else {
        cacheBuilder.from(className);
      }
      if (annotation.size() > 0) {
        cacheBuilder.maximumSize(annotation.size());
      }
      if (annotation.expiresAfterSeconds() > 0) {
        cacheBuilder.expireAfterWrite(annotation.expiresAfterSeconds());
      }
      cache = cacheBuilder.build();
      namespaces.put(className, cache);
    }

    return cache;
  }

  private Class getClass(MethodInvocation invocation) {
    Class clazz = invocation.getThis().getClass();
    if (!clazz.isAnnotationPresent(Cached.class)) { // TODO Check te actual class name
      // Workaround for Guice adding some kind of enhancer object before the actual DAO
      // Check the superclass
      clazz = clazz.getSuperclass();
      if (!clazz.isAnnotationPresent(Cached.class)) {
        throw new InternalServerErrorRestException("Could not find Cached annotation");
      }
    }
    return clazz;
  }

}
