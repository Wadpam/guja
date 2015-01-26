package com.wadpam.guja.persist;

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
 * Manage the start and stop of the GAE Datastore.
 * In generate the Datastore does not need any specific actions to the started or stopped.
 *
 * @author osandstrom
 */
public class MardaoDatastorePersistService implements PersistService {
  static final Logger LOGGER = LoggerFactory.getLogger(MardaoDatastorePersistService.class);

  private DatastoreService datastore;

  public MardaoDatastorePersistService() {
    LOGGER.debug("<init>");
  }

  public DatastoreService getDatastore() {
    return datastore;
  }

  @Override
  public void start() {
    // No actions are needed to start the Datastore
    LOGGER.debug("Start");
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  public void stop() {
    // No actions are needed to stop the Datastore
    LOGGER.debug("Stop");
  }
}
