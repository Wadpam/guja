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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

/**
 * Custom Jackson ObjectMapper.
 * Supports injection in both Guice and Jersey context.
 *
 * @author mattiaslevin
 */
@Provider
@Singleton
public class NonEmptyObjectMapperProvider extends AbstractObjectMapperProvider {

  @Override
  void configureMapper(ObjectMapper mapper) {

    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    // Ignore unknown fields during deserialization
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // Do not serialize null values and empty values
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    // Ignore setterless properties
    mapper.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);
  }

}