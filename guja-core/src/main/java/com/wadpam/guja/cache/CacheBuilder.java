package com.wadpam.guja.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Created by sosandstrom on 2014-12-18.
 */
public interface CacheBuilder<K, V> {

  CacheBuilder expireAfterWrite(int seconds);

  CacheBuilder from(String spec);

  CacheBuilder maximumSize(long size);

  LoadingCache<K, V> build(CacheLoader<K, V> cacheLoader);

  Cache<K, V> build();

}
