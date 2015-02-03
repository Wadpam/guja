package com.wadpam.guja.jackson;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Based custom Jackson ObjectMapper.
 * Supports injection in both Guice and Jersey context.
 **
 * @author mattiaslevin
 */
@Provider
@Singleton
public abstract class AbstractObjectMapperProvider implements ContextResolver<ObjectMapper>, com.google.inject.Provider<ObjectMapper> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractObjectMapperProvider.class);

  final private ObjectMapper defaultObjectMapper;

  public AbstractObjectMapperProvider() {
    defaultObjectMapper = new ObjectMapper();
    configureMapper(defaultObjectMapper);
  }

  // Jersey context
  @Override
  public ObjectMapper getContext(Class<?> type) {
    LOGGER.debug("Get ObjectMapper Jersey context");
    return defaultObjectMapper;
  }

  // Guice provider
  @Override
  public ObjectMapper get() {
    LOGGER.debug("Get ObjectMapper Guice context");
    return defaultObjectMapper;
  }

  /**
   * Sub classes should override and configure the mapper.
   */
  abstract void configureMapper(ObjectMapper mapper);

}