package com.wadpam.guja.cache;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import net.sf.mardao.dao.InMemorySupplier;
import net.sf.mardao.dao.Supplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

import static org.easymock.EasyMock.*;

public class CacheMethodInterceptorTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheMethodInterceptorTest.class);

  private Injector injector;

  private DPersonDaoBean mockDao;
  private MockCacheBuilderProvider cacheBuilderProvider;
  private LoadingCache mockLoadingCache;
  private Cache mockCache;

  private DPerson person;


  @Before
  public void setUp() throws Exception {

    person = new DPerson();
    person.setFirstName("Mattias");
    person.setLastName("Levin");

    mockDao = createMock(DPersonDaoBean.class);

    cacheBuilderProvider = new MockCacheBuilderProvider();
    mockLoadingCache = cacheBuilderProvider.getMockLoadingCache();
    mockCache = cacheBuilderProvider.getMockCache();

    injector = Guice.createInjector(new Module() {
      @Override
      public void configure(Binder binder) {

        binder.bind(Supplier.class).to(InMemorySupplier.class);
        binder.bind(DPersonDaoBean.class).toInstance(mockDao);

        binder.bindInterceptor(
            Matchers.only(mockDao),
            Matchers.any(),
            new DaoCrudCachingInterceptor(new MockCacheBuilderProvider()));

      }
    });

  }

  @After
  public void tearDown() throws Exception {
    verify(mockDao, mockLoadingCache, mockCache);
  }

  @Test
  public void testGet() throws Exception {
    LOGGER.info("Cache GET");

    person.setId(55L);
    expect(mockDao.get(55L)).andReturn(person).once();
    expect(mockCache.get(55L, new Callable() {
      @Override
      public Object call() throws Exception {
        return null;
      }
    })).andReturn(Optional.absent()).once();

    replay(mockDao, mockLoadingCache, mockCache);

    DPersonDaoBean dao = injector.getInstance(DPersonDaoBean.class);
    dao.get(55L);

  }


}