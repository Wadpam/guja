package com.wadpam.guja.provider;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.Collections;

/**
 * Provider a jsr105 based cached backed by GAEs memcache.
 * @author mattiaslevin
 */
@Singleton
public class CacheProvider implements Provider<Cache> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheProvider.class);

  private Cache cache;

  public CacheProvider() {

    try {
      CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
      cache = cacheFactory.createCache(Collections.emptyMap());
    } catch (CacheException e) {
      LOGGER.error("Failed to create cache {}", e);
    }

  }

  @Override
  public Cache get() {
    return cache;
  }

}
