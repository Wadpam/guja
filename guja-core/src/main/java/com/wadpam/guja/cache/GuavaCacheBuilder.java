package com.wadpam.guja.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Created by sosandstrom on 2014-12-18.
 */
public class GuavaCacheBuilder<K, V> implements LoadingCacheBuilder<K, V> {

    private CacheBuilder cacheBuilder = CacheBuilder.newBuilder();

    @Override
    public LoadingCacheBuilder from(String spec) {
        this.cacheBuilder = CacheBuilder.from(spec);
        return this;
    }

    @Override
    public LoadingCacheBuilder maximumSize(long size) {
        cacheBuilder = cacheBuilder.maximumSize(size);
        return this;
    }

    @Override
    public LoadingCache<K, V> build(CacheLoader<K, V> cacheLoader) {
        return cacheBuilder.build(cacheLoader);
    }
}
