package com.wadpam.guja.cache.annotations;

import com.google.inject.*;
import com.wadpam.guja.cache.CacheBuilder;
import com.wadpam.guja.cache.DelegatingCrudDao;
import com.wadpam.guja.cache.GuavaCacheBuilderProvider;
import net.sf.mardao.dao.CrudDao;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.verify;

public abstract class AbstractCacheMethodInterceptorTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCacheMethodInterceptorTest.class);

  protected Injector injector;

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
        binder.bind(CacheBuilder.class).toProvider(GuavaCacheBuilderProvider.class);

      }
    }, new CacheAnnotationsModule());

    dao = injector.getInstance(DelegatingCrudDao.class);
  }

  @After
  public void tearDown() throws Exception {
    verify(mockDao);
  }

}