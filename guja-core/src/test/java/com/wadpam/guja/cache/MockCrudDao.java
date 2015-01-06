package com.wadpam.guja.cache;

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

  final private CrudDao<String, Long> mockDelegate;

  @Inject
  public MockCrudDao(CrudDao<String, Long> mock) {
    this.mockDelegate = mock;
  }

  @Override
  public int count(Object o) {
    return mockDelegate.count(o);
  }

  @Cached
  @Crud
  @Override
  public Long put(Object parentKey, Long aLong, String s) throws IOException {
    return mockDelegate.put(parentKey, aLong, s);
  }

  @Cached
  @Crud
  @Override
  public String get(Object parentKey, Long aLong) throws IOException {
    return mockDelegate.get(parentKey, aLong);
  }

  public String get(Long aLong) throws IOException {
    return get(null, aLong);
  }

  @Cached
  @Crud
  @Override
  public void delete(Object parentKey, Long aLong) throws IOException {
    mockDelegate.delete(parentKey, aLong);
  }

  public void delete(Long aLong) throws IOException {
    delete(null, aLong);
  }

  @Cached
  @Crud
  @Override
  public CursorPage<String> queryPage(Object ancestorKey, int i, String s) {
    return mockDelegate.queryPage(ancestorKey, i, s);
  }

  public CursorPage<String> queryPage(int i, String s) {
    return queryPage(null, i, s);
  }

//  public MockCrudDao getMockDelegate() {
//    return mockDelegate;
//  }
}
