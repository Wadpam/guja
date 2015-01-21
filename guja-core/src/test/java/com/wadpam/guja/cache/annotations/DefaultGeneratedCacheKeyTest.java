package com.wadpam.guja.cache.annotations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultGeneratedCacheKeyTest {

  DefaultGeneratedCacheKey key;

  @Before
  public void setUp() throws Exception {

    Long[] params = {1L, 2L};
    key = new DefaultGeneratedCacheKey(params);

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testEquals() throws Exception {

    Long[] params = {1L, 2L};
    DefaultGeneratedCacheKey key2 = new DefaultGeneratedCacheKey(params);
    assertTrue(key.equals(key2));

    Long[] params2 = {1L, 3L};
    key2 = new DefaultGeneratedCacheKey(params2);
    assertFalse(key.equals(key2));

    Long[] params3 = {1L, 2L, 3L};
    key2 = new DefaultGeneratedCacheKey(params3);
    assertFalse(key.equals(key2));

  }

}