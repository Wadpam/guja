package com.wadpam.guja.cache.annotations;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Cache put method interceptor.
 *
 * @author mattiaslevin
 */
public class CachePutMethodInterceptor extends AbstractCacheMethodInterceptor {
  static final Logger LOGGER = LoggerFactory.getLogger(CachePutMethodInterceptor.class);

  @Inject
  public CachePutMethodInterceptor(Provider<CacheResolver> cacheResolverProvider, Provider<CacheKeyGenerator> defaultCacheKeyGeneratorProvider) {
    super(cacheResolverProvider, defaultCacheKeyGeneratorProvider);
  }

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {

    final Method method = methodInvocation.getMethod();
    final CachePut annotation = method.getAnnotation(CachePut.class);
    checkNotNull(annotation);
    final CacheKeyInvocationContext<CachePut> context = new GuiceCacheKeyInvocationContext<>(methodInvocation, annotation, annotation.cacheName());

    LOGGER.trace("Cache PUT {}", context.getMethod().getName());

    // get cache
    final Cache<GeneratedCacheKey, Optional<?>> cache = getCache(context, methodInvocation);

    // get key
    final CacheKeyGenerator keyGenerator = getCacheKeyGenerator(methodInvocation, annotation.cacheKeyGenerator());
    final GeneratedCacheKey key = keyGenerator.generateCacheKey(context);

    // get value to cache
    final Object value = context.getValueParameter().getValue();

    if (!annotation.afterInvocation()) {
      cacheValue(key, value, cache);
    }

    final Object result = methodInvocation.proceed();

    if (annotation.afterInvocation()) {
      cacheValue(key, value, cache);
    }

    return result;
  }

  private static void cacheValue(final GeneratedCacheKey key, final Object value, final Cache<GeneratedCacheKey, Optional<?>> cache) {
    cache.put(key, null != value ? Optional.of(value) : Optional.absent());
  }

}
