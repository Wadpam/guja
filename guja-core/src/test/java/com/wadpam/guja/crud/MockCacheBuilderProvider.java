package com.wadpam.guja.crud;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Provider;
import com.wadpam.guja.cache.CacheBuilder;
import com.wadpam.guja.cache.CacheBuilderProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easymock.EasyMock.createMock;

/**
 * A mock cache builder provider that can build mock CacheBuilder, LoadingCache and Cache.
 * @author mattiaslevin
 */
public class MockCacheBuilderProvider implements CacheBuilderProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(MockCacheBuilderProvider.class);

  private LoadingCache mockLoadingCache;
  private Cache mockCache;

  public MockCacheBuilderProvider() {
    mockLoadingCache = createMock(LoadingCache.class);
    mockCache = createMock(Cache.class);
  }

  @Override
  public CacheBuilder get() {
    return new CacheBuilder() {
      @Override
      public CacheBuilder expireAfterWrite(int seconds) {
        return this;
      }

      @Override
      public CacheBuilder from(String spec) {
        return this;
      }

      @Override
      public CacheBuilder maximumSize(long size) {
        return this;
      }

      @Override
      public LoadingCache build(CacheLoader cacheLoader) {
        LOGGER.debug("builder mock LoadingCache");
        return mockLoadingCache;
      }

      @Override
      public Cache build() {
        LOGGER.debug("builder mock Cache");
        return mockCache;
      }
    };
  }


  public Cache getMockCache() {
    return mockCache;
  }

  public LoadingCache getMockLoadingCache() {
    return mockLoadingCache;
  }

}
