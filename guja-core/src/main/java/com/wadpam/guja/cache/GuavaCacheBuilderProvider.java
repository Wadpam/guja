package com.wadpam.guja.cache;

/**
 * Created by sosandstrom on 2014-12-18.
 */
public class GuavaCacheBuilderProvider implements CacheBuilderProvider {
  @Override
  public CacheBuilder get() {
    return new GuavaCacheBuilder();
  }
}
