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
public class CacheRemoveEntityMethodInterceptor extends AbstractCacheMethodInterceptor {
  static final Logger LOGGER = LoggerFactory.getLogger(CacheRemoveEntityMethodInterceptor.class);

  @Inject
  public CacheRemoveEntityMethodInterceptor(Provider<CacheResolver> cacheResolverProvider, Provider<CacheKeyGenerator> defaultCacheKeyGeneratorProvider) {
    super(cacheResolverProvider, defaultCacheKeyGeneratorProvider);
  }

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {

    final Method method = methodInvocation.getMethod();
    final CacheRemove annotation = method.getAnnotation(CacheRemove.class);
    checkNotNull(annotation);
    final CacheKeyInvocationContext<CacheRemove> context = new GuiceCacheKeyInvocationContext<>(methodInvocation, annotation, annotation.cacheName());

    LOGGER.trace("Cache REMOVE {}", context.getMethod().getName());

    // get cache
    final Cache<GeneratedCacheKey, Optional<?>> cache = getCache(context);

    // get key
    final CacheKeyGenerator keyGenerator = getCacheKeyGenerator(methodInvocation, annotation.cacheKeyGenerator());
    final GeneratedCacheKey key = keyGenerator.generateCacheKey(context);

    if (!annotation.afterInvocation()) {
      invalidateCache(key, cache);
    }

    final Object result = methodInvocation.proceed();

    if (annotation.afterInvocation()) {
      invalidateCache(key, cache);
    }

    return result;
  }

  private static void invalidateCache(GeneratedCacheKey key, Cache<GeneratedCacheKey, Optional<?>> cache) {
    cache.invalidate(key);
  }

}
