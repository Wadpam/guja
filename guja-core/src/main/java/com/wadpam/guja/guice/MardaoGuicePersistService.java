package com.wadpam.guja.guice;

/*
 * #%L
 * guja-core
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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.inject.persist.PersistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom
 *         Date: 1/19/14 Time: 8:12 PM
 */
public class MardaoGuicePersistService implements PersistService {
  static final Logger LOGGER = LoggerFactory.getLogger(MardaoGuicePersistService.class);
  private DatastoreService datastore;

  public MardaoGuicePersistService() {
    LOGGER.debug("<init>");
  }

  public DatastoreService getDatastore() {
    return datastore;
  }

  @Override
  public void start() {
    LOGGER.debug("start");
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  public void stop() {
    LOGGER.debug("stop");
  }
}
