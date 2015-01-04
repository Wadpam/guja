package com.wadpam.guja.cache;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import net.sf.mardao.dao.Cached;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

public class CacheMethodInterceptorTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheMethodInterceptorTest.class);

  private Injector injector;


  @Before
  public void setUp() throws Exception {

    injector = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {

        binder.bind(MockCrudDao.class);

        binder.bindInterceptor(
            Matchers.annotatedWith(Cached.class),
            Matchers.annotatedWith(Cached.class),
            new CacheMethodInterceptor(new GuavaCacheBuilderProvider()));
      }
    });

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGet() throws Exception {

    MockCrudDao dao = injector.getInstance(MockCrudDao.class);
    MockCrudDao mockDao = dao.getMockDelegate();

    expect(mockDao.get(1L)).andReturn("1").once();
    expect(mockDao.get(2L)).andReturn("2").once();

    replay(mockDao);

    String value = dao.get(1L);
    assertTrue("1".equals(value));

    value = dao.get(1L);
    assertTrue("1".equals(value));

    value = dao.get(2L);
    assertTrue("2".equals(value));

    verify(mockDao);

  }

  @Test
  public void testPut() throws Exception {
    LOGGER.info("Cache PUT");


    MockCrudDao dao = injector.getInstance(MockCrudDao.class);
    MockCrudDao mockDao = dao.getMockDelegate();

    expect(mockDao.put("1")).andReturn(1L).once();

    replay(mockDao);

    Long id = dao.put("1");
    assertTrue(id.equals(1L));

    String value = dao.get(1L);
    assertTrue("1".equals(value));

    verify(mockDao);

  }


  @Test
  public void testDelete() throws Exception {
    LOGGER.info("Cache PUT");

    MockCrudDao dao = injector.getInstance(MockCrudDao.class);
    MockCrudDao mockDao = dao.getMockDelegate();

    expect(mockDao.put("1")).andReturn(1L).once();
    mockDao.delete(1L);
    expect(mockDao.get(1L)).andReturn(null).once();
    expect(mockDao.put("1")).andReturn(1L).once();

    replay(mockDao);

    Long id = dao.put("1");
    assertTrue(id.equals(1L));

    String value = dao.get(1L);
    assertTrue("1".equals(value));

    dao.delete(1L);

    value = dao.get(1L);
    assertTrue(null == value);

    id = dao.put("1");
    assertTrue(id.equals(1L));

    value = dao.get(1L);
    assertTrue("1".equals(value));


    verify(mockDao);

  }




}