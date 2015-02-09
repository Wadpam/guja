package com.wadpam.guja.config;

import com.google.inject.AbstractModule;
import com.wadpam.guja.api.ContactResource;
import com.wadpam.guja.dao.DContactDaoBean;

/**
 * Guice contact module.
 *
 * @author mattiaslevin
 */
public class GujaContactModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(ContactResource.class);
    bind(DContactDaoBean.class);

  }
}
