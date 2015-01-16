package com.wadpam.guja.cache.annotations;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.inject.*;
import com.wadpam.guja.cache.CacheBuilder;
import com.wadpam.guja.cache.DelegatingCrudDao;
import com.wadpam.guja.cache.GuavaCacheBuilderProvider;
import com.wadpam.guja.cache.PagedCachedMockCrudDao;
import com.wadpam.guja.oauth2.api.OAuth2UserResource;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.CrudDao;
import org.junit.Test;

import javax.cache.annotation.GeneratedCacheKey;

import java.util.concurrent.Callable;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CacheResultMethodInterceptorTest extends AbstractCacheMethodInterceptorTest {

  @Test
  public void testInvoke() throws Exception {
    final String parentKey = null;

    expect(mockDao.get(parentKey, 1L)).andReturn("1").once();
    expect(mockDao.get(parentKey, 2L)).andReturn("2").once();

    replay(mockDao);

    String value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));

    value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));

    value = dao.get(parentKey, 2L);
    assertTrue("2".equals(value));
  }

  @Test
  public void testResultNull() throws Exception {
    final String parentKey = null;

    expect(mockDao.get(parentKey, 1L)).andReturn(null).times(1);

    replay(mockDao);

    String value = dao.get(parentKey, 1L);
    assertTrue(null == value);

    value = dao.get(parentKey, 1L);
    assertTrue(null == value);

    value = dao.get(parentKey, 1L);
    assertTrue(null == value);

  }

  @Test
  public void testCacheConfig() throws Exception {

    mockDao = createMock(CrudDao.class);
    final CacheBuilder mockCacheBuilder = createMock(CacheBuilder.class);
    Cache mockCache = createMock(Cache.class);

    injector = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {

        binder.bind(DelegatingCrudDao.class);
        binder.bind(new TypeLiteral<CrudDao<String, Long>>() {}).toInstance(mockDao);
        binder.bind(CacheBuilder.class).toProvider(new Provider<CacheBuilder>() {
          @Override
          public CacheBuilder get() {
            return mockCacheBuilder;
          }
        });

      }
    }, new CacheAnnotationsModule());

    dao = injector.getInstance(DelegatingCrudDao.class);

    expect(mockCacheBuilder.name("DelegatingCrudDao.findByName")).andReturn(mockCacheBuilder).once();
    expect(mockCacheBuilder.expireAfterWrite(60 * 10)).andReturn(mockCacheBuilder).once();
    expect(mockCacheBuilder.build()).andReturn(mockCache).once();
    expect(mockCache.get(anyObject(GeneratedCacheKey.class), anyObject(Callable.class))).andReturn(Optional.of("Levin")).once();

    replay(mockDao, mockCacheBuilder, mockCache);

    String lastName = dao.findByName("Mattias");
    assertTrue(lastName.equals("Levin"));

    verify(mockDao, mockCacheBuilder, mockCache);

  }

  @Test
  public void testPage() throws Exception {
    final String parentKey = null;

    expect(mockDao.put(parentKey, 1L, "1")).andReturn(1L).once();
    expect(mockDao.queryPage(parentKey, 1, null)).andReturn(new CursorPage<String>()).once();

    replay(mockDao);

    Long id = dao.put(parentKey, 1L, "1");
    assertTrue(id.equals(1L));

    String value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));

    dao.queryPage(parentKey, 1, null);

    // non-overlap between page keys and entity keys relies on different types Long / Integer:
    assertFalse(Long.valueOf(1L).equals(Integer.valueOf(1)));
  }

  @Test
  public void testQueryPageNoCache() throws Exception {
    final String parentKey = null;

    // Caching is disabled by default

    CursorPage<String> page = new CursorPage<>();
    page.setCursorKey(null);
    page.setTotalSize(3);
    page.setItems(ImmutableList.of("1", "2", "3"));

    expect(mockDao.queryPage(parentKey, 10, null)).andReturn(page).times(2);

    replay(mockDao);

    CursorPage<String> result = dao.queryPage(parentKey, 10, null);
    assertTrue(result.getItems().equals(page.getItems()));

    // second call should skip the cache
    result = dao.queryPage(parentKey, 10, null);
    assertTrue(result.getItems().equals(page.getItems()));

  }

  @Test
  public void testQueryPage() throws Exception {

    mockDao = createMock(CrudDao.class);

    // Create a crud dao with page caching enabled
    injector = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {

        binder.bind(PagedCachedMockCrudDao.class);
        binder.bind(new TypeLiteral<CrudDao<String, Long>>() {
        }).toInstance(mockDao);

        binder.bind(CacheBuilder.class).toProvider(GuavaCacheBuilderProvider.class);

      }
    }, new CacheAnnotationsModule());

    dao = injector.getInstance(PagedCachedMockCrudDao.class);

    final String parentKey = null;

    CursorPage<String> page = new CursorPage<>();
    page.setCursorKey(null);
    page.setTotalSize(3);
    page.setItems(ImmutableList.of("1", "2", "3"));

    expect(mockDao.queryPage(parentKey, 10, null)).andReturn(page).once();

    replay(mockDao);

    CursorPage<String> result = dao.queryPage(parentKey, 10, null);
    assertTrue(result.getItems().equals(page.getItems()));

    // second call should hit the cache
    result = dao.queryPage(parentKey, 10, null);
    assertTrue(result.getItems().equals(page.getItems()));

  }

}