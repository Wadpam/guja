package com.wadpam.guja.cache;

import com.google.inject.Inject;
import com.wadpam.guja.crud.DelegatingCrudDao;
import net.sf.mardao.dao.Cached;
import net.sf.mardao.dao.CrudDao;

/**
 * A page caching mock dao.
 * @author mattiaslevin
 */
@Cached(cachePages = true)
public class PagedCachedMockCrudDao extends DelegatingCrudDao {

  @Inject
  public PagedCachedMockCrudDao(CrudDao<String, Long> mock) {
    super(mock);
  }

}
