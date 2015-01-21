package com.wadpam.guja.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Cache builder.
 *
 * @author sosandstrom
 */
public interface CacheBuilder<K, V> {

  CacheBuilder expireAfterWrite(int seconds);

  CacheBuilder name(String spec);

  CacheBuilder maximumSize(long size);

  LoadingCache<K, V> build(CacheLoader<K, V> cacheLoader);

  Cache<K, V> build();

}
