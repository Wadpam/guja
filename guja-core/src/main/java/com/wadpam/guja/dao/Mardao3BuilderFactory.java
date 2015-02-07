package com.wadpam.guja.dao;

import net.sf.mardao.dao.AbstractDao;

import java.io.Serializable;

/**
 * Created by sosandstrom on 2015-02-07.
 */
public class Mardao3BuilderFactory implements DaoBuilderFactory {
    @Override
    public <T, ID extends Serializable, C extends Serializable, PT, PID extends Serializable> ParentDaoBuilder<T, ID, C, PT, PID> create(Object dao, Object parentDao) {
        return (ParentDaoBuilder<T, ID, C, PT, PID>) new Mardao3Builder<T, ID, PT, PID>((AbstractDao<T, ID>) dao, (AbstractDao<PT, PID>) parentDao);
    }
}
