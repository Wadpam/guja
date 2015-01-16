package com.wadpam.guja.cache.annotations;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;

/**
 * Based cached method interceptor.
 *
 * @author mattiaslevin
 */
public abstract class AbstractCacheMethodInterceptor implements MethodInterceptor {

  protected final Provider<CacheResolver> cacheResolverProvider;
  protected final Provider<CacheKeyGenerator> defaultCacheKeyGeneratorProvider;

  public AbstractCacheMethodInterceptor(Provider<CacheResolver> cacheResolverProvider, Provider<CacheKeyGenerator> defaultCacheKeyGeneratorProvider) {
    this.cacheResolverProvider = cacheResolverProvider;
    this.defaultCacheKeyGeneratorProvider = defaultCacheKeyGeneratorProvider;
  }

  protected <A extends Annotation> Cache<GeneratedCacheKey, Optional<?>> getCache(CacheKeyInvocationContext<A> context) {
    return cacheResolverProvider.get().resolveCache(context);
  }


  protected CacheKeyGenerator getCacheKeyGenerator(MethodInvocation methodInvocation, Class<? extends CacheKeyGenerator> methodCacheKeyGeneratorClass) {

    try {

      if (!methodCacheKeyGeneratorClass.equals(CacheKeyGenerator.class)) {
        // The annotation will return the CacheKeyGenerator class by default
        return methodCacheKeyGeneratorClass.newInstance();
      }

      Class clazz = GuiceCacheKeyInvocationContext.getThisClass(methodInvocation);
      if (clazz.isAnnotationPresent(CacheDefaults.class)) {
        Class<? extends CacheKeyGenerator> cacheKeyGeneratorClass = ((CacheDefaults)clazz.getAnnotation(CacheDefaults.class)).cacheKeyGenerator();
        if (!cacheKeyGeneratorClass.equals(CacheKeyGenerator.class)) {
          // The annotation will return the CacheKeyGenerator class by default
          return cacheKeyGeneratorClass.newInstance();
        }
      }

      return defaultCacheKeyGeneratorProvider.get();

    } catch (Exception e) {
      throw new AnnotationFormatError("Invalid cache key generator class " + methodCacheKeyGeneratorClass.getName());
    }

  }

}
