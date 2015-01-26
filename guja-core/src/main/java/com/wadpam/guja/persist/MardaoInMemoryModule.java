package com.wadpam.guja.persist;

import com.google.inject.AbstractModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import net.sf.mardao.dao.InMemorySupplier;
import net.sf.mardao.dao.Supplier;

/**
 * Configure Guice and Mardao to use the in-memory supplier.
 *
 * @author mattiaslevin
 */
public class MardaoInMemoryModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(UnitOfWork.class).to(MardaoInMemoryUnitOfWork.class);
    bind(PersistService.class).to(MardaoInMemoryPersistService.class);

    bind(Supplier.class).to(InMemorySupplier.class);

  }

}
