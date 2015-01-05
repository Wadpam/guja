package com.wadpam.guja.config;

/*
 * #%L
 * guja-base
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.inject.AbstractModule;
import com.wadpam.guja.GAEAdminTaskQueue;
import com.wadpam.guja.admintask.AdminTaskQueue;
import com.wadpam.guja.api.GAEBlobResource;
import com.wadpam.guja.cache.CacheBuilder;
import com.wadpam.guja.cache.CacheBuilderProvider;
import com.wadpam.guja.cache.MemCacheBuilderProvider;
import com.wadpam.guja.environment.GAEServerEnvironment;
import com.wadpam.guja.environment.ServerEnvironment;


/**
 * Configure Guice module.
 *
 * @author mattiaslevin
 */
public class GujaGAEModule extends AbstractModule {


  @Override
  protected void configure() {

    bind(CacheBuilderProvider.class).to(MemCacheBuilderProvider.class);
    bind(CacheBuilder.class).toProvider(MemCacheBuilderProvider.class);

    bind(ServerEnvironment.class).to(GAEServerEnvironment.class);

    bind(GAEBlobResource.class);

    bind(AdminTaskQueue.class).to(GAEAdminTaskQueue.class);

  }

}
