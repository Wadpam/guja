package com.wadpam.guja.dao;

import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.AbstractDao;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by sosandstrom on 2015-02-07.
 */
public class Mardao3Builder<T, ID extends Serializable, PT, PID extends Serializable>
        implements ParentDaoBuilder<T, ID, String, PT, PID> {

    private final AbstractDao<T, ID> dao;
    private final AbstractDao<PT, PID> parentDao;
    private Object parentKey = null;
    private int pageSize = 10;

    public Mardao3Builder(AbstractDao<T, ID> dao, AbstractDao<PT, PID> parentDao) {
        this.dao = dao;
        this.parentDao = parentDao;
    }

    public Mardao3Builder(AbstractDao<T, ID> dao) {
        this(dao, null);
    }

    @Override
    public Object getKey(PID parentId) {
        return parentDao.getKey(null, parentId);
    }

    @Override
    public int count(Object parentKey) throws IOException {
        return dao.count(parentKey);
    }

    @Override
    public DaoBuilder query() {
        return new Mardao3Builder(dao, parentDao);
    }

    @Override
    public DaoBuilder parent(Object parentKey) {
        this.parentKey = parentKey;
        return this;
    }

    @Override
    public DaoBuilder equals(String field, Object value) {
        return null;
    }

    @Override
    public T asSingle() throws IOException {
        CursorPage<T> page = dao.queryPage(parentKey, 1, null);
        return page.getItems().isEmpty() ? null : page.getItems().iterator().next();
    }

    @Override
    public Iterable<T> asIterable() throws IOException {
        return null;
    }

    @Override
    public DaoBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public QueryPage<T, String> asPage(String cursorKey) {
        CursorPage<T> cursorPage = dao.queryPage(parentKey, pageSize, cursorKey);
        QueryPage<T, String> queryPage = new QueryPage<>();
        queryPage.setTotalSize(cursorPage.getTotalSize());
        queryPage.setCursorKey(cursorPage.getCursorKey());
        queryPage.setItems(cursorPage.getItems());

        return queryPage;
    }

    @Override
    public void setParentKey(T entity, Object parentKey) {
        dao.setParentKey(entity, parentKey);
    }

    @Override
    public T get(Object parentKey, ID id) throws IOException {
        return dao.get(parentKey, id);
    }

    @Override
    public ID put(Object parentKey, ID id, T entity) throws IOException {
        return dao.put(parentKey, id, entity);
    }

    @Override
    public void delete(Object parentKey, ID id) throws IOException {
        dao.delete(parentKey, id);
    }
}
