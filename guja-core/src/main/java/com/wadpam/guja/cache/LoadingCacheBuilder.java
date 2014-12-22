package com.wadpam.guja.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Created by sosandstrom on 2014-12-18.
 */
public interface LoadingCacheBuilder<K, V> {
    LoadingCacheBuilder expireAfterWrite(int seconds);
    LoadingCacheBuilder from(String spec);
    LoadingCacheBuilder maximumSize(long size);
    LoadingCache<K, V> build(CacheLoader<K, V> cacheLoader);
}
