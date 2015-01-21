package com.wadpam.guja.cache.annotations;


import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

/**
 * Generate a cache key based on parameter list.
 *
 * @author mattiaslevin
 */
public class DefaultCacheKeyGenerator implements CacheKeyGenerator {

  @Override
  public GeneratedCacheKey generateCacheKey(CacheKeyInvocationContext<? extends Annotation> cacheKeyInvocationContext) {

    final CacheInvocationParameter[] keyParameters = cacheKeyInvocationContext.getKeyParameters();

    final Object[] parameters = new Object[keyParameters.length];
    for (int index = 0; index < keyParameters.length; index++) {
      parameters[index] = keyParameters[index].getValue();
    }

    return new DefaultGeneratedCacheKey(parameters);

  }

}
