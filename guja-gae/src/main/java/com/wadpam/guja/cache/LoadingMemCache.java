package com.wadpam.guja.cache;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;

import java.util.concurrent.ExecutionException;

/**
 * Created by sosandstrom on 2014-12-21.
 */
public class LoadingMemCache<K, V> extends AbstractLoadingCache<K, V> {

    private final CacheLoader<K, V> cacheLoader;
    private final MemcacheService syncCache;
    private final String namespace;
    private final int secondsDelay;

    public LoadingMemCache(CacheLoader<K, V> cacheLoader, MemcacheService syncCache, String namespace, int secondsDelay) {
        this.cacheLoader = cacheLoader;
        this.syncCache = syncCache;
        this.namespace = namespace;
        this.secondsDelay = secondsDelay;
    }

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
                e.printStackTrace();
            }
        }
//        System.out.println("get " + k + " = " + value);
        return value;
    }

    @Override
    public V getIfPresent(Object o) {
        V returnValue = (V) syncCache.get(o);
//        System.out.println("getIfPresent " + o + " = " + returnValue);
        return returnValue;
    }

    @Override
    public void invalidateAll() {
        syncCache.clearAll();
    }

    @Override
    public void put(K key, V value) {
//        System.out.println("put " + key + " = " + value);
        syncCache.put(key, value, Expiration.byDeltaSeconds(secondsDelay));
    }

}
