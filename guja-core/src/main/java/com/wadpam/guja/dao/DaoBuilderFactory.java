package com.wadpam.guja.dao;

import java.io.Serializable;

/**
 * Created by sosandstrom on 2015-02-07.
 */
public interface DaoBuilderFactory {
    <T, ID extends Serializable, C extends Serializable, PT, PID extends Serializable> ParentDaoBuilder<T, ID, C, PT, PID>
        create(Object dao, Object parentDao);
}
