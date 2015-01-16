package com.wadpam.guja.cache.annotations;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;

import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

/**
 * Resolve a cache invocation context to a cache implementation.
 *
 * @author mattiaslevin
 */
public interface CacheResolver {

  Cache<GeneratedCacheKey, Optional<?>> resolveCache(CacheInvocationContext<? extends Annotation> cacheInvocationContext,
                                                     Optional<CacheConfig> cacheConfig);

}
