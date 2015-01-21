package com.wadpam.guja.cache.annotations;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.annotation.*;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Cache put method interceptor.
 *
 * @author mattiaslevin
 */
public class CacheRemoveAllMethodInterceptor extends AbstractCacheMethodInterceptor {
  static final Logger LOGGER = LoggerFactory.getLogger(CacheRemoveAllMethodInterceptor.class);

  @Inject
  public CacheRemoveAllMethodInterceptor(Provider<CacheResolver> cacheResolverProvider, Provider<CacheKeyGenerator> defaultCacheKeyGeneratorProvider) {
    super(cacheResolverProvider, defaultCacheKeyGeneratorProvider);
  }

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {

    final Method method = methodInvocation.getMethod();
    final CacheRemoveAll annotation = method.getAnnotation(CacheRemoveAll.class);
    checkNotNull(annotation);
    final CacheKeyInvocationContext<CacheRemoveAll> context = new GuiceCacheKeyInvocationContext<>(methodInvocation, annotation, annotation.cacheName());

    LOGGER.trace("Cache REMOVE_ALL {}", context.getMethod().getName());

    // get cache
    final Cache<GeneratedCacheKey, Optional<?>> cache = getCache(context, methodInvocation);

    if (!annotation.afterInvocation()) {
      cache.invalidateAll();
    }

    final Object result = methodInvocation.proceed();

    if (annotation.afterInvocation()) {
      cache.invalidateAll();
    }

    return result;
  }

}
