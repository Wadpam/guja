package com.wadpam.guja.cache;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.wadpam.guja.crud.CachedCrudResource;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.AbstractDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sosandstrom on 2014-12-19.
 */
public class LoadingMemCacheTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoadingMemCacheTest.class);


  private static final String KEY = "some_key";
  private static final String VALUE = "some_value";

  AbstractDao<String, Long> daoMock;
  CachedCrudResource<String, Long, AbstractDao<String, Long>> resource;
  final CacheBuilder<Long, String> cacheBuilder = new MemCacheBuilder();

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());

  @Test
  public void testCrudCache() throws IOException, URISyntaxException {
    for (int i = 1; i < 50; i++) {
      expect(daoMock.get(Long.valueOf(i))).andReturn(Long.toString(i)).once();
    }

    replay(daoMock);

    for (int n = 0; n < 50; n++) {
      for (int i = 1; i < 50; i++) {

        Response actual = resource.read(Long.valueOf(i));
        assertEquals(Long.toString(i), actual.getEntity());
      }
    }
  }

  @Test
  public void testUpdateCache() throws IOException, URISyntaxException {

    for (int i = 1; i < 50; i++) {
      expect(daoMock.put("Updated" + (i - 1))).andReturn(Long.valueOf(i - 1)).once();
      expect(daoMock.get(Long.valueOf(i))).andReturn(Long.toString(i)).once();
    }
    expect(daoMock.put("Updated49")).andReturn(Long.valueOf(49)).once();

    replay(daoMock);

    for (int n = 0; n < 50; n++) {
      for (int i = 0; i < 50; i++) {

        // forced update
        if (i == n) {
          resource.update(Long.valueOf(i), "Updated" + i);
        }

        Response actual = resource.read(Long.valueOf(i));

        assertEquals((i <= n ? "Updated" : "") + Long.toString(i), actual.getEntity());
      }
    }
  }

  @Test
  public void testDeleteCreateCache() throws IOException, URISyntaxException {

    for (int i = 0; i < 50; i++) {
      expect(daoMock.get(Long.valueOf(i))).andReturn(Long.toString(i)).once();
      daoMock.delete(Long.valueOf(i));
      expect(daoMock.put("Created" + i)).andReturn(Long.valueOf(i)).once();
    }

    replay(daoMock);

    for (int n = 0; n < 50; n++) {
      for (int i = 0; i < 50; i++) {

        // forced delete
        if (10 == n) {
          resource.delete(Long.valueOf(i));
        } else if (20 == n) {
          resource.create("Created" + i);
        }

        Response actual = resource.read(Long.valueOf(i));

        if (n < 10) {
          assertEquals(Long.toString(i), actual.getEntity());
        } else if (n < 20) {
          assertEquals(404, actual.getStatus());
        } else {
          assertEquals("Created" + Long.toString(i), actual.getEntity());
        }
      }
    }
  }

  @Test
  public void testPageCache() {
    String cursorKey = null;
    for (int p = 3; p < 9; p++) {
      CursorPage<String> page = new CursorPage<>();
      page.setCursorKey("abc" + p);
      ImmutableList.Builder<String> builder = ImmutableList.builder();
      for (int i = 0; i < p; i++) {
        builder.add(Integer.toString(p));
      }
      page.setItems(builder.build());
      expect(daoMock.queryPage(p, cursorKey)).andReturn(page).once();
      cursorKey = page.getCursorKey();
    }

    replay(daoMock);

    for (int n = 0; n < 10; n++) {
      cursorKey = null;
      for (int p = 3; p < 9; p++) {
        Response response = resource.readPage(p, cursorKey);
        CursorPage<String> page = (CursorPage<String>) response.getEntity();
        cursorKey = page.getCursorKey();
        assertEquals(p, page.getItems().size());
        assertEquals(Integer.toString(p), page.getItems().iterator().next());
      }
    }
  }

  @Test
  public void testRefresh() throws Exception {

    expect(daoMock.get(1L)).andReturn("1").once();
    replay(daoMock);

    LoadingCache<Long, String> loadingCache = cacheBuilder.build(new CacheLoader<Long, String>() {
      @Override
      public String load(Long key) throws Exception {
        return daoMock.get(key);
      }
    });

    loadingCache.refresh(1L);

    assertTrue("1".equals(loadingCache.getIfPresent(1L)));

  }


  @Before
  public void setUp() {
    helper.setUp();
    daoMock = createMock(AbstractDao.class);
    resource = new CachedCrudResource<>(daoMock, cacheBuilder, 100, getClass().getName());
  }

  @After
  public void tearDown() {
    verify(daoMock);
    helper.tearDown();
  }


}
