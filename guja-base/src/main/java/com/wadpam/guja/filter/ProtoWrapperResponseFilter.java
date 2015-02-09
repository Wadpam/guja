package com.wadpam.guja.filter;

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

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;


/**
 * Wrap response with content type x-protobuf in a container containing the response code.
 *
 * @author mattiaslevin
 */
@Provider
@Singleton
public class ProtoWrapperResponseFilter implements ContainerResponseFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProtoWrapperResponseFilter.class);

  public static final String APPLICATION_X_PROTOBUF = "application/x-protobuf";
  public static final MediaType APPLICATION_X_PROTOBUF_TYPE = new MediaType("application", "x-protobuf");


  @Override
  public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

    Collection<Object> contentTypes = response.getHttpHeaders().get("Content-Type");
    //LOGGER.debug("content types {}", contentTypes);
    if (null != contentTypes) {
      for (Object contentType : contentTypes) {
        if (contentType.equals(APPLICATION_X_PROTOBUF_TYPE) && !shouldSkipWrapping(response.getEntity())) {
          LOGGER.debug("Content type is x-protobuf, wrap response entity");
          ResponseCodeEntityWrapper<Object> wrapper = new ResponseCodeEntityWrapper<>(response.getStatus(), response.getEntity());
          response.setEntity(wrapper, response.getEntityType());
          break;
        }
      }

    }

    return response;
  }

  private static boolean shouldSkipWrapping(Object entity) {

    if (null == entity) {
      return true;
    }

    Class<?> clazz = entity.getClass();
    LOGGER.debug("class {}", clazz);
    if (clazz.isAnnotationPresent(SkipProtoWrapper.class)) {
      return true;
    } else if (Collection.class.isAssignableFrom(clazz)) {
      // Check the elements in the collection
      Collection collection = (Collection) entity;
      if (!collection.isEmpty()) {
        Object element = collection.iterator().next();
        LOGGER.debug("elements class {}", element.getClass());
        if (element.getClass().isAnnotationPresent(SkipProtoWrapper.class)) {
          return true;
        }
      }
    }

    return false;
  }

}
