package com.wadpam.guja.cache;

/**
 * Created by sosandstrom on 2014-12-18.
 */
public class MemCacheBuilderProvider implements CacheBuilderProvider {
    @Override
    public LoadingCacheBuilder get() {
        return new MemCacheBuilder();
    }
}
