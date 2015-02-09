package com.wadpam.guja.dao;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by sosandstrom on 2015-02-07.
 */
public interface DaoBuilder<T, ID extends Serializable, C extends Serializable> {

    int count(Object parentKey) throws IOException;

    T get(Object parentKey, ID id) throws IOException;

    ID put(Object parentKey, ID id, T entity) throws IOException;

    void delete(Object parentKey, ID id) throws IOException;

    DaoBuilder query();

    DaoBuilder parent(Object parentKey);

    DaoBuilder equals(String field, Object value);

    T asSingle() throws IOException;

    Iterable<T> asIterable() throws IOException;

    DaoBuilder pageSize(int pageSize);

    QueryPage<T, C> asPage(C cursorKey);

    void setParentKey(T entity, Object parentKey);
}
