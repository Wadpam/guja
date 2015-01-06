package com.wadpam.guja.cache;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Cached;
import net.sf.mardao.dao.Crud;
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
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Method interceptor for adding caching to core crud methods.
 *
 * @author sosandstrom
 * @author mattiaslevin
 */
public class CacheMethodInterceptor implements MethodInterceptor {
  static final Logger LOGGER = LoggerFactory.getLogger(CacheMethodInterceptor.class);

  private final ConcurrentMap<String, Cache<Triple, Optional<?>>> namespaces = new ConcurrentHashMap<>();

  private final Provider<CacheBuilder> cacheBuilderProvider;
  public CacheMethodInterceptor(Provider<CacheBuilder> cacheBuilderProvider) {
    this.cacheBuilderProvider = cacheBuilderProvider;
  }

  private static final String PUT_METHOD_NAME = "put";
  private static final String DELETE_METHOD_NAME = "delete";
  private static final String GET_METHOD_NAME = "get";
  private static final String GET_PAGE_METHOD_NAME = "queryPage";

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {

    final Class clazz = getClass(invocation);
    Cache<Triple, Optional<?>> cache = getCacheInstance(clazz);

    final Object[] args = invocation.getArguments();
    final Triple triple = new Triple(args);
    final Method method = invocation.getMethod();
    LOGGER.trace("invoking on {}, isAnnotated {}", method, method.isAnnotationPresent(Cached.class));

    if (PUT_METHOD_NAME.equals(method.getName()) && method.isAnnotationPresent(Crud.class)) {
      LOGGER.trace("   put");
      final Object id = invocation.proceed();
      args[1] = id;
      final Object entity = args[2];
      args[2] = null;
      checkNotNull(id);
      checkNotNull(entity);
      cache.put(new Triple(args), Optional.of(entity));
      return id;
    }

    if (DELETE_METHOD_NAME.equals(method.getName()) && method.isAnnotationPresent(Crud.class)) {
      LOGGER.info("   delete");
      invocation.proceed();
      final Object id = args[1];
      checkNotNull(id);
      cache.put(triple, Optional.absent());
      return null;
    }

    final Optional<?> optionalEntity = cache.get(triple, new Callable<Optional<?>>() {
      @Override
      public Optional<?> call() throws Exception {
        LOGGER.trace("Loading for {}({})", method.getName(), triple);
        try {
          final Object entity = invocation.proceed();
          return null != entity ? Optional.of(entity) : Optional.absent();
        } catch (Throwable throwable) {
          LOGGER.error("Failed to populate cache {} {}", clazz.getName(), throwable);
          throw new ExecutionException("During invocation: ", throwable);
        }
      }
    });
    return null != optionalEntity && optionalEntity.isPresent() ? optionalEntity.get() : null;
  }

  private Cache<Triple, Optional<?>> getCacheInstance(Class clazz) {
    final String className = clazz.getName();

    Cache<Triple, Optional<?>> cache = namespaces.get(className);
    if (null == cache) {
      final Cached annotation = (Cached) clazz.getAnnotation(Cached.class);
      LOGGER.debug("Build new dao cache for {}", className);
      CacheBuilder<Triple, Optional<?>> cacheBuilder = cacheBuilderProvider.get();
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
      Cache<Triple, Optional<?>> existingCache = namespaces.putIfAbsent(className, cache);
      if (null != existingCache) {
        cache = existingCache;
      }
    }

    return cache;
  }

  private Class getClass(MethodInvocation invocation) {
    Class clazz = invocation.getThis().getClass();
    if (clazz.getName().contains("$$EnhancerByGuice$$")) {
      // Workaround for Guice adding some kind of enhancer object before the actual DAO
      // Check the superclass
      clazz = clazz.getSuperclass();
      if (!clazz.isAnnotationPresent(Cached.class)) {
        throw new InternalServerErrorRestException("Could not find Cached annotation");
      }
    }
    return clazz;
  }

  public static class Triple implements Serializable {
    protected final Object first, second, third;

    Triple(Object[] args) {
      this.first = 0 < args.length ? args[0] : null;
      this.second = 1 < args.length ? args[1] : null;
      this.third = 2 < args.length ? args[2] : null;
    }

    Triple(Object first, Object second, Object third) {
      this.first = first;
      this.second = second;
      this.third = third;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Triple) {
        Triple other = (Triple) o;
        if (!Objects.equals(this.first, other.first)) {
          return false;
        }
        if (!Objects.equals(this.second, other.second)) {
          return false;
        }
        return Objects.equals(this.third, other.third);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return (31 * (31 * (null != first ? first.hashCode() : 0)) + (null != second ? second.hashCode() : 0)) +
              (null != third ? third.hashCode() : 0);
    }

    @Override
    public String toString() {
      return "Triple[" + first + ',' + second + ',' + third + ']';
    }
  }
}
