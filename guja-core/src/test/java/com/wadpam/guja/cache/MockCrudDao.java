package com.wadpam.guja.cache;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.Cached;
import net.sf.mardao.dao.Crud;
import net.sf.mardao.dao.CrudDao;

import java.io.IOException;

import static org.easymock.EasyMock.createMock;

/**
 * CrudDao for unit testing.
 * @author mattiaslevin
 */
@Cached
public class MockCrudDao implements CrudDao<String, Long> {

  final private MockCrudDao mockDelegate;

  @Inject
  public MockCrudDao() {
    this.mockDelegate = createMock(MockCrudDao.class);
  }

  @Cached
  @Crud
  @Override
  public Long put(String s) throws IOException {
    return mockDelegate.put(s);
  }

  @Cached
  @Crud
  @Override
  public String get(Long aLong) throws IOException {
    return mockDelegate.get(aLong);
  }

  @Cached
  @Crud
  @Override
  public void delete(Long aLong) throws IOException {
    mockDelegate.delete(aLong);
  }

  @Cached
  @Crud
  @Override
  public CursorPage<String> queryPage(int i, String s) {
    return mockDelegate.queryPage(i, s);
  }

  public MockCrudDao getMockDelegate() {
    return mockDelegate;
  }
}
