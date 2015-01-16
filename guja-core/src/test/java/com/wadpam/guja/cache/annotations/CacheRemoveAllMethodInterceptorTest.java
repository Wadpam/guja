package com.wadpam.guja.cache.annotations;

import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertTrue;

public class CacheRemoveAllMethodInterceptorTest extends CachePutMethodInterceptorTest {

  @Test
  public void testInvoke() throws Exception {

    final String parentKey = null;

    expect(mockDao.put(parentKey, 1L, "1")).andReturn(1L).once();
    expect(mockDao.get(parentKey, 1L)).andReturn(null).once();
    expect(mockDao.put(parentKey, 1L, "1")).andReturn(1L).once();

    replay(mockDao);

    Long id = dao.put(parentKey, 1L, "1");
    assertTrue(id.equals(1L));

    String value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));

    dao.removeAll();

    value = dao.get(parentKey, 1L);
    assertTrue(null == value);

    value = dao.get(parentKey, 1L);
    assertTrue(null == value);

    id = dao.put(parentKey, 1L, "1");
    assertTrue(id.equals(1L));

    value = dao.get(parentKey, 1L);
    assertTrue("1".equals(value));

  }

}