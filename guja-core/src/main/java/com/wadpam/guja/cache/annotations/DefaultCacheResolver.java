package com.wadpam.guja.cache.annotations;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wadpam.guja.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Resolve a cache based on cache builder.
 * Built caches are cached for increased performance.
 *
 * @author mattiaslevin
 */
@Singleton
public class DefaultCacheResolver implements CacheResolver {
  static final Logger LOGGER = LoggerFactory.getLogger(DefaultCacheResolver.class);

  private final ConcurrentMap<String, Cache<GeneratedCacheKey, Optional<?>>> namespaces = new ConcurrentHashMap<>();

  private final Provider<CacheBuilder> cacheBuilderProvider;

  @Inject
  public DefaultCacheResolver(Provider<CacheBuilder> cacheBuilderProvider) {
    this.cacheBuilderProvider = cacheBuilderProvider;
  }

  @Override
  public Cache<GeneratedCacheKey, Optional<?>> resolveCache(CacheInvocationContext<? extends Annotation> cacheInvocationContext) {

    final String cacheName = cacheInvocationContext.getCacheName();
    Cache<GeneratedCacheKey, Optional<?>> cache = namespaces.get(cacheName);
    if (null == cache) {
      LOGGER.debug("Build new dao cache for name {}", cacheName);

      // TODO Do we need to set size and expirationTime to default values?
      cache = cacheBuilderProvider.get()
          .name(cacheName)
          .build();

      Cache<GeneratedCacheKey, Optional<?>> existingCache = namespaces.putIfAbsent(cacheName, cache);
      if (null != existingCache) {
        cache = existingCache;
      }
    }

    return cache;
  }

}
