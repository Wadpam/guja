package com.wadpam.guja.cache;

/**
 * @author sosandstrom
 */
public class GuavaCacheBuilderProvider implements CacheBuilderProvider {
  @Override
  public CacheBuilder get() {
    return new GuavaCacheBuilder();
  }
}
