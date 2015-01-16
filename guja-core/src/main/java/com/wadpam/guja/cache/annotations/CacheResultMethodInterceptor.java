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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Cache put method interceptor.
 *
 * @author mattiaslevin
 */
public class CacheResultMethodInterceptor extends AbstractCacheMethodInterceptor {
  static final Logger LOGGER = LoggerFactory.getLogger(CacheResultMethodInterceptor.class);

  @Inject
  public CacheResultMethodInterceptor(Provider<CacheResolver> cacheResolverProvider, Provider<CacheKeyGenerator> defaultCacheKeyGeneratorProvider) {
    super(cacheResolverProvider, defaultCacheKeyGeneratorProvider);
  }

  @Override
  public Object invoke(final MethodInvocation methodInvocation) throws Throwable {

    final Method method = methodInvocation.getMethod();
    final CacheResult annotation = method.getAnnotation(CacheResult.class);
    checkNotNull(annotation);
    final CacheKeyInvocationContext<CacheResult> context = new GuiceCacheKeyInvocationContext<>(methodInvocation, annotation, annotation.cacheName());

    LOGGER.debug("Cache RESULT {}", context.getMethod().getName());

    // get cache
    final Cache<GeneratedCacheKey, Optional<?>> cache = getCache(context);

    // get key
    final CacheKeyGenerator keyGenerator = getCacheKeyGenerator(methodInvocation, annotation.cacheKeyGenerator());
    final GeneratedCacheKey key = keyGenerator.generateCacheKey(context);

    Object result;
    if(!annotation.skipGet()) {

      Optional<?> value = cache.get(key, new Callable<Optional<?>>() {
        @Override
        public Optional<?> call() throws Exception {
          LOGGER.trace("Loading for {}({})", method.getName(), key);
          try {
            final Object value = methodInvocation.proceed();
            return null != value ? Optional.of(value) : Optional.absent();
          } catch (Throwable throwable) {
            LOGGER.error("Failed to populate cache {} {}", method.getName(), throwable);
            throw new ExecutionException("During invocation: ", throwable);
          }
        }
      });

      return value.isPresent() ? value.get() : null;
    }

    // Always run the annotated method and update the cache
    result = methodInvocation.proceed();
    cache.put(key, null != result ? Optional.of(result) : Optional.absent());
    return result;

  }

}
