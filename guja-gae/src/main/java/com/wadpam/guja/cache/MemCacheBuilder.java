package com.wadpam.guja.cache;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Created by sosandstrom on 2014-12-21.
 */
public class MemCacheBuilder<K, V> implements LoadingCacheBuilder<K, V> {

    private String namespace;
    private int secondsDelay = 3600;

    @Override
    public LoadingCacheBuilder expireAfterWrite(int seconds) {
        this.secondsDelay = seconds;
        return this;
    }

    @Override
    public LoadingCacheBuilder from(String spec) {
        this.namespace = spec;
        return this;
    }

    @Override
    public LoadingCacheBuilder maximumSize(long size) {
        return this;
    }

    @Override
    public LoadingCache<K, V> build(CacheLoader<K, V> cacheLoader) {
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService(namespace);
        return new LoadingMemCache<>(cacheLoader, syncCache, namespace, secondsDelay);
    }
}
