package com.wadpam.guja.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by sosandstrom on 2014-12-18.
 */
public class GuavaCacheBuilder<K, V> implements CacheBuilder<K, V> {

  // TODO handle generics
  private com.google.common.cache.CacheBuilder cacheBuilder = com.google.common.cache.CacheBuilder.newBuilder();

  @Override
  public CacheBuilder expireAfterWrite(int seconds) {
    this.cacheBuilder = cacheBuilder.expireAfterWrite(seconds, TimeUnit.SECONDS);
    return this;
  }

  @Override
  public CacheBuilder from(String ignored) {
    this.cacheBuilder = com.google.common.cache.CacheBuilder.from("");
    return this;
  }

  @Override
  public CacheBuilder maximumSize(long size) {
    cacheBuilder = cacheBuilder.maximumSize(size);
    return this;
  }

  @Override
  public LoadingCache<K, V> build(CacheLoader<K, V> cacheLoader) {
    return cacheBuilder.build(cacheLoader);
  }

  @Override
  public Cache<K, V> build() {
    return cacheBuilder.build();
  }

}
