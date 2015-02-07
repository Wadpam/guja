package com.wadpam.guja.persist;

import com.google.inject.AbstractModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import com.wadpam.guja.dao.DaoBuilderFactory;
import com.wadpam.guja.dao.Mardao3BuilderFactory;
import net.sf.mardao.dao.DatastoreSupplier;
import net.sf.mardao.dao.Supplier;

/**
 * Configure Guice and Mardao to user GAE datastore supplier.
 *
 * @author mattiaslevin
 */
public class Mardao3DatastoreModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(UnitOfWork.class).to(MardaoDatastoreUnitOfWork.class);
    bind(PersistService.class).to(MardaoDatastorePersistService.class);

    bind(DaoBuilderFactory.class).to(Mardao3BuilderFactory.class);
    bind(Supplier.class).to(DatastoreSupplier.class);

  }

}
