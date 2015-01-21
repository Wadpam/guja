package com.wadpam.guja.cache;

import com.google.inject.Inject;
import net.sf.mardao.core.CacheConfig;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.CrudDao;

import javax.cache.annotation.*;
import java.io.IOException;

/**
 * Delegating CrudDao.
 *
 * @author mattiaslevin
 */
@CacheDefaults(cacheName = "DelegatingCrudDao")
public class DelegatingCrudDao implements CrudDao<String, Long> {

  final private CrudDao<String, Long> delegateDao;

  @Inject
  public DelegatingCrudDao(CrudDao<String, Long> delegate) {
    this.delegateDao = delegate;
  }

  @Override
  public int count(Object o) {
    return delegateDao.count(o);
  }

  @CachePut
  @Override
  public Long put(@CacheKey Object parentKey, @CacheKey Long aLong, @CacheValue String s) throws IOException {
    return delegateDao.put(parentKey, aLong, s);
  }

  @CacheResult
  @Override
  public String get(Object parentKey, Long aLong) throws IOException {
    return delegateDao.get(parentKey, aLong);
  }

  public String get(Long aLong) throws IOException {
    return get(null, aLong);
  }

  @CacheRemove
  @Override
  public void delete(Object parentKey, Long aLong) throws IOException {
    delegateDao.delete(parentKey, aLong);
  }

  public void delete(Long aLong) throws IOException {
    delete(null, aLong);
  }

  @Override
  public CursorPage<String> queryPage(Object ancestorKey, int i, String s) {
    return delegateDao.queryPage(ancestorKey, i, s);
  }

  public CursorPage<String> queryPage(int i, String s) {
    return queryPage(null, i, s);
  }

  @CacheRemoveAll
  public void removeAll() {
    // Do nothing
  }

  @CacheConfig(expiresAfterSeconds = 60 * 10)
  @CacheResult(cacheName = "DelegatingCrudDao.findByName")
  public String findByName(String name) {
    return "Levin";
  }

}
