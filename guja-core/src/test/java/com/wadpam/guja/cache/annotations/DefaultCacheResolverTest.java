package com.wadpam.guja.cache.annotations;

import com.google.common.cache.Cache;
import com.google.inject.Provider;
import com.wadpam.guja.cache.CacheBuilder;
import com.wadpam.guja.cache.GuavaCacheBuilderProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class DefaultCacheResolverTest {

  CacheResolver cacheResolver;
  CacheKeyInvocationContext mockContext;

  @Before
  public void setUp() throws Exception {
    mockContext = createMock(CacheKeyInvocationContext.class);
    cacheResolver = new DefaultCacheResolver(new GuavaCacheBuilderProvider());
  }

  @After
  public void tearDown() throws Exception {
    verify(mockContext);
  }

  @Test
  public void testResolveCache() throws Exception {

    expect(mockContext.getCacheName()).andReturn("name").times(2);

    replay(mockContext);

    Cache cache1 = cacheResolver.resolveCache(mockContext);
    Cache cache2 = cacheResolver.resolveCache(mockContext);

    assertTrue(cache1.equals(cache2));

  }
}