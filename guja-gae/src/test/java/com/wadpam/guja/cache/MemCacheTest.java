package com.wadpam.guja.cache;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertTrue;

public class MemCacheTest {

  private static final String KEY = "some_key";
  private static final String VALUE = "some_value";

  final Cache<String, String> cache = new MemCacheBuilder().build();

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testGetIfPresent() throws Exception {

    cache.put(KEY, VALUE);

    assertTrue(VALUE.equals(cache.getIfPresent(KEY)));
    assertTrue(null == cache.getIfPresent("wrong key"));

  }

  @Test
  public void testGetAllPresent() throws Exception {

    cache.put("key1", "value1");
    cache.put("key2", "value2");
    cache.put("key3", "value3");

    List<String> keys = ImmutableList.of("key1", "key2", "key4");
    Map<String, String> results = cache.getAllPresent(keys);

    assertTrue(results.size() == 2);
    assertTrue(null != results.get("key1"));
    assertTrue(null == results.get("key3"));

  }

  @Test
  public void testGet() throws Exception {

    String value = cache.get("missing_key", new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "value";
      }
    });

    assertTrue("value".equals(value));
    assertTrue("value".equals(cache.getIfPresent("missing_key")));
  }

  @Test
  public void testInvalidate() throws Exception {

    cache.put(KEY, VALUE);
    cache.invalidate(KEY);

    assertTrue(null == cache.getIfPresent(KEY));

  }

  @Test
  public void testInvalidateAll() throws Exception {

    cache.put("key1", "value1");
    cache.put("key2", "value2");
    cache.put("key3", "value3");

    List<String> keys = ImmutableList.of("key1", "key2", "key4");
    cache.invalidateAll(keys);

    assertTrue(null == cache.getIfPresent("key1"));
    assertTrue(null != cache.getIfPresent("key3"));

  }

}