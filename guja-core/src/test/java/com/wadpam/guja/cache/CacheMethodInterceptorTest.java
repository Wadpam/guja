package com.wadpam.guja.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.wadpam.guja.crud.MockCacheBuilderProvider;
import net.sf.mardao.dao.Cached;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

public class CacheMethodInterceptorTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheMethodInterceptorTest.class);

  private Injector injector;

  private MockCacheBuilderProvider cacheBuilderProvider;
  private LoadingCache mockLoadingCache;
  private Cache mockCache;

  @Before
  public void setUp() throws Exception {

    cacheBuilderProvider = new MockCacheBuilderProvider();
    mockLoadingCache = cacheBuilderProvider.getMockLoadingCache();
    mockCache = cacheBuilderProvider.getMockCache();

    injector = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {

        binder.bind(MockCrudDao.class);

        binder.bindInterceptor(
            Matchers.annotatedWith(Cached.class),
            Matchers.annotatedWith(Cached.class),
            new CacheMethodInterceptor(new MockCacheBuilderProvider()));
      }
    });

  }

  @After
  public void tearDown() throws Exception {
    verify(mockLoadingCache, mockCache);
  }

  @Test
  public void testGet() throws Exception {
    LOGGER.info("Cache GET");

    replay(mockLoadingCache, mockCache);

    MockCrudDao dao = injector.getInstance(MockCrudDao.class);
    String value = dao.get(1L);

    assertTrue(value.equals("1"));


  }


}