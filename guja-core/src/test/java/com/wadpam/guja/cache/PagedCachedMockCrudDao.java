package com.wadpam.guja.cache;

import com.google.inject.Inject;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.CrudDao;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResult;

/**
 * A page caching mock dao.
 * @author mattiaslevin
 */
@CacheDefaults(cacheName = "PagedCachedMockCrudDao")
public class PagedCachedMockCrudDao extends DelegatingCrudDao {

  @Inject
  public PagedCachedMockCrudDao(CrudDao<String, Long> mock) {
    super(mock);
  }

  @CacheResult
  @Override
  public CursorPage<String> queryPage(Object ancestorKey, int i, String s) {
    return super.queryPage(ancestorKey, i, s);
  }

}
