package com.wadpam.guja.cache;

import com.google.common.collect.ImmutableList;
import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.wadpam.guja.crud.DelegatingCrudDao;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.Cached;
import net.sf.mardao.dao.CrudDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CacheMethodInterceptorTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheMethodInterceptorTest.class);

  private Injector injector;

  DelegatingCrudDao dao;
  CrudDao<String, Long> mockDao;

  @Before
  public void setUp() throws Exception {

    mockDao = createMock(CrudDao.class);

    injector = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {

        binder.bind(DelegatingCrudDao.class);
        binder.bind(new TypeLiteral<CrudDao<String, Long>>() {}).toInstance(mockDao);

        binder.bindInterceptor(
            Matchers.annotatedWith(Cached.class),
            Matchers.annotatedWith(Cached.class),
            new CacheMethodInterceptor(new GuavaCacheBuilderProvider()));
      }
    });

    dao = injector.getInstance(DelegatingCrudDao.class);
  }

  @After
  public void tearDown() throws Exception {
    verify(mockDao);
  }

  @Test
  public void testGet() throws Exception {
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
  public void testPut() throws Exception {
    LOGGER.info("Cache PUT");
    final String parentKey = null;

    expect(mockDao.put(parentKey, 1L, "1")).andReturn(1L).once();

    replay(mockDao);

    Long id = dao.put(parentKey, 1L, "1");
    assertTrue(id.equals(1L));

    String value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));
  }

  @Test
  public void testPage() throws Exception {
    LOGGER.info("Cache page");
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
  public void testDelete() throws Exception {
    LOGGER.info("Cache DELETE");
    final String parentKey = null;

    expect(mockDao.put(parentKey, 1L, "1")).andReturn(1L).times(2);
    mockDao.delete(parentKey, 1L);

    replay(mockDao);

    Long id = dao.put(parentKey, 1L, "1");
    assertTrue(id.equals(1L));

    String value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));

    dao.delete(parentKey, 1L);

    value = dao.get(1L);
    assertTrue(null == value);

    id = dao.put(parentKey, 1L, "1");
    assertTrue(id.equals(1L));

    value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));
  }

  @Test
  public void testQueryPageNoCache() throws Exception {
    LOGGER.info("Cache queryPage");
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
        binder.bind(new TypeLiteral<CrudDao<String, Long>>() {}).toInstance(mockDao);

        binder.bindInterceptor(
            Matchers.annotatedWith(Cached.class),
            Matchers.annotatedWith(Cached.class),
            new CacheMethodInterceptor(new GuavaCacheBuilderProvider()));
      }
    });

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