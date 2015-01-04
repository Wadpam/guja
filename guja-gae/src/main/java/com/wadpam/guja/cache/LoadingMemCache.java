package com.wadpam.guja.cache;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sosandstrom on 2014-12-21.
 */
public class LoadingMemCache<K, V> extends AbstractLoadingCache<K, V> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoadingMemCache.class);

  private final CacheLoader<K, V> cacheLoader;
  private final MemcacheService syncCache;
  private final String namespace; // already used when creating the MemcacheService
  private final Expiration expiration;

  public LoadingMemCache(CacheLoader<K, V> cacheLoader, MemcacheService syncCache, String namespace, int secondsDelay) {
    this.cacheLoader = cacheLoader;
    this.syncCache = syncCache;
    this.namespace = namespace;
    this.expiration = Expiration.byDeltaSeconds(secondsDelay);
  }

  // Cache<K, V> interface implementation

  @Override
  public V getIfPresent(Object key) {
    V returnValue = (V) syncCache.get(key);
    //LOGGER.debug("getIfPresent {}", returnValue);
    return returnValue;
  }

  @Override
  public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
    checkNotNull(valueLoader);

    V value = getIfPresent(key);
    if (null == value) {
      try {
        value = valueLoader.call();
        if (null != value) {
          put(key, value);
        }
      } catch (Exception e) {
        LOGGER.warn("Failed to load value for the cache {}", e);
      }
    }

    return value;
  }

  @Override
  public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
    Map<K, V> result = (Map<K, V>) syncCache.getAll(Lists.newArrayList(keys));
    return ImmutableMap.copyOf(result);
  }

  @Override
  public void put(K key, V value) {
    //LOGGER.debug("put {} = {}", key, value);
    syncCache.put(key, value, expiration);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    syncCache.putAll(ImmutableMap.copyOf(m), expiration);
  }

  @Override
  public void invalidate(Object key) {
    syncCache.delete(key);
  }

  @Override
  public void invalidateAll(Iterable<?> keys) {
    syncCache.deleteAll(ImmutableList.copyOf(keys));
  }

  @Override
  public void invalidateAll() {
    syncCache.clearAll();
  }


  // LoadingCache<K, V> interface implementation

  @Override
  public V get(K k) throws ExecutionException {
    V value = getIfPresent(k);
    if (null == value) {
      try {
        value = cacheLoader.load(k);
        if (null != value) {
          put(k, value);
        }
      } catch (Exception e) {
        LOGGER.warn("Failed to load value for the cache {}", e);
      }
    }
    //LOGGER.debug("get {} = {}", k, value);
    return value;
  }

  @Override
  public void refresh(K key) {
    // TODO Change to using admin task (not so easy)
    refreshCacheValueTask(key);
  }

  public void refreshCacheValueTask(K key) {
    V value = getIfPresent(key);
    if (null == value) {
      try {
        value = cacheLoader.load(key);
        put(key, value);
      } catch (Exception e) {
        LOGGER.debug("Failed refreshing cache {}", e);
        // Do nothing
      }
    } else {
      try {
        ListenableFuture<V> task = cacheLoader.reload(key, value);
        value = task.get(5, TimeUnit.SECONDS);
        put(key, value);
      } catch (Exception e) {
        LOGGER.debug("Failed refreshing cache {}", e);
        // Do nothing
      }
    }
  }

}
