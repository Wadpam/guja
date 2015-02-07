package com.wadpam.guja.dao;

import java.io.Serializable;

/**
 * Created by sosandstrom on 2015-02-07.
 */
public interface ParentDaoBuilder<T, ID extends Serializable, C extends Serializable, PT, PID extends Serializable> extends DaoBuilder<T, ID, C> {
    Object getKey(PID parentId);
}
