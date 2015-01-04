package com.wadpam.guja.cache;

import com.google.common.collect.ImmutableList;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.Cached;
import net.sf.mardao.dao.Crud;
import net.sf.mardao.dao.CrudDao;

import java.io.IOException;

/**
 * CrudDao for unit testing.
 * @author mattiaslevin
 */
@Cached
public class MockCrudDao implements CrudDao<String, Long> {

  @Cached
  @Crud
  @Override
  public Long put(String s) throws IOException {
    return Long.parseLong(s);
  }

  @Cached
  @Crud
  @Override
  public String get(Long aLong) throws IOException {
    return aLong.toString();
  }

  @Cached
  @Crud
  @Override
  public void delete(Long aLong) throws IOException {
    // Do nothing
  }

  @Cached
  @Crud
  @Override
  public CursorPage<String> queryPage(int i, String s) {

    CursorPage<String> page = new CursorPage<>();
    page.setTotalSize(2);
    page.setCursorKey(null);
    page.setItems(ImmutableList.of("1", "1"));

    return page;
  }
}
