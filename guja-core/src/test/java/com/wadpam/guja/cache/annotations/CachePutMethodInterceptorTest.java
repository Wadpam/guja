package com.wadpam.guja.cache.annotations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertTrue;

public class CachePutMethodInterceptorTest extends AbstractCacheMethodInterceptorTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(CachePutMethodInterceptorTest.class);

  @Test
  public void testPut() throws Exception {
    LOGGER.info("Cache PUT");
    final String parentKey = null;

    expect(mockDao.put(parentKey, 1L, "1")).andReturn(1L).once();
    expect(mockDao.put(parentKey, 1L, "2")).andReturn(1L).once();

    replay(mockDao);

    Long id = dao.put(parentKey, 1L, "1");
    assertTrue(id.equals(1L));

    String value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));

    id = dao.put(parentKey, 1L, "2");
    assertTrue(id.equals(1L));

    value = dao.get(parentKey, 1L);
    assertTrue("2".equals(value));

  }

}