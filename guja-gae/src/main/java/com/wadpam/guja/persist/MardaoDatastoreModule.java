package com.wadpam.guja.persist;

import com.google.inject.AbstractModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import net.sf.mardao.dao.DatastoreSupplier;
import net.sf.mardao.dao.Supplier;

/**
 * Configure Guice to use Mardao Datastore provider.
 * Set PersistService and unit of work.
 *
 * @author mattiaslevin
 */
public class MardaoDatastoreModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(UnitOfWork.class).to(MardaoDatastoreUnitOfWork.class);
    bind(PersistService.class).to(MardaoDatastorePersistService.class);

    bind(Supplier.class).to(DatastoreSupplier.class);

  }

}
