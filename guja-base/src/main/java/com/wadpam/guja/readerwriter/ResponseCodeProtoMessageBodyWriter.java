package com.wadpam.guja.readerwriter;

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

import com.wadpam.guja.filter.ProtoWrapperResponseFilter;
import com.wadpam.guja.filter.ResponseCodeEntityWrapper;
import com.wadpam.guja.proto.ResponseCodeProtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Convert all other entities with x-protobuf Content-Type to just its http response code.
 *
 * @author mattiaslevin
 */
@Provider
@Singleton
@Produces(ProtoWrapperResponseFilter.APPLICATION_X_PROTOBUF)
public class ResponseCodeProtoMessageBodyWriter implements MessageBodyWriter<ResponseCodeEntityWrapper> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseCodeProtoMessageBodyWriter.class);


  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    LOGGER.debug("Media type {} entity type {}", mediaType, genericType);
    return mediaType.equals(ProtoWrapperResponseFilter.APPLICATION_X_PROTOBUF_TYPE) &&
        type == ResponseCodeEntityWrapper.class;
  }

  @Override
  public long getSize(ResponseCodeEntityWrapper responseCodeEntityWrapper, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    // Not used
    return 0;
  }

  @Override
  public void writeTo(ResponseCodeEntityWrapper responseCodeEntityWrapper, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    LOGGER.debug("Serialize to protocol buffer");

    ResponseCodeProtos.ResponseCode.Builder builder = ResponseCodeProtos.ResponseCode.newBuilder();

    builder.setResponseCode(responseCodeEntityWrapper.getResponseCode())
        .build()
        .writeTo(entityStream);

  }
}
