package com.wadpam.guja.cache.annotations;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import javax.cache.annotation.*;

/**
 * Include this module to enable cache annotations.
 *
 * @author mattiaslevin
 */
public class CacheAnnotationsModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(CacheResolver.class).to(DefaultCacheResolver.class);
    bind(CacheKeyGenerator.class).to(DefaultCacheKeyGenerator.class);

    bindInterceptor(Matchers.any(), Matchers.annotatedWith(CachePut.class),
        new CachePutMethodInterceptor(getProvider(CacheResolver.class), getProvider(CacheKeyGenerator.class)));

    bindInterceptor(Matchers.any(), Matchers.annotatedWith(CacheResult.class),
        new CacheResultMethodInterceptor(getProvider(CacheResolver.class), getProvider(CacheKeyGenerator.class)));

    bindInterceptor(Matchers.any(), Matchers.annotatedWith(CacheRemove.class),
        new CacheRemoveEntityMethodInterceptor(getProvider(CacheResolver.class), getProvider(CacheKeyGenerator.class)));

    bindInterceptor(Matchers.any(), Matchers.annotatedWith(CacheRemoveAll.class),
        new CacheRemoveAllMethodInterceptor(getProvider(CacheResolver.class), getProvider(CacheKeyGenerator.class)));

  }

}
