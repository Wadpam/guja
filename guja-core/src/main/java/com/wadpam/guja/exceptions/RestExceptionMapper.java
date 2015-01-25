package com.wadpam.guja.exceptions;

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

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Map rest exception to a http response code and entity.
 *
 * @author mattiaslevin
 */
@Provider
@Singleton
public class RestExceptionMapper implements ExceptionMapper<RestException> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionMapper.class);

  @Context
  HttpHeaders httpHeaders;

  @Override
  public Response toResponse(RestException exception) {
    //LOGGER.debug("Map exception - {}", exception.getMessage());
    //LOGGER.debug("Header context {}", httpHeaders.getAcceptableMediaTypes());

    ImmutableMap.Builder<String, String> errorMessage = new ImmutableMap.Builder<>();
    if (null != exception.getJson()) {
      errorMessage.putAll(exception.getJson());
    }

    if (null != exception.getMessage()) {
      errorMessage.put("message", exception.getMessage());
    }

    errorMessage.put("responseCode", String.valueOf(exception.getStatus().getStatusCode()));

    return Response.status(exception.getStatus())
            .entity(errorMessage.build())
            // Throws exception if type is MediaType.WILDCARD_TYPE
            .type(getAcceptableMediaTypeNotWildcard())
            .build();
  }

  private MediaType getAcceptableMediaTypeNotWildcard() {
    MediaType type = httpHeaders.getAcceptableMediaTypes().get(0);
    return MediaType.WILDCARD_TYPE.equals(type) ? null : type;
  }
}
