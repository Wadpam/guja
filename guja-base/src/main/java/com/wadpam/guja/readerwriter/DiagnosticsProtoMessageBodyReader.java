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

import com.wadpam.guja.api.DiagnosticsResource;
import com.wadpam.guja.proto.DiagnosticsProtos;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Convert diagnostic information from protocol buffer format to an entity.
 *
 * @author matiaslevin
 */
public class DiagnosticsProtoMessageBodyReader implements MessageBodyReader<DiagnosticsResource.Diagnostics> {


  public static final String APPLICATION_X_PROTOBUF = "application/x-protobuf";
  public static final MediaType APPLICATION_X_PROTOBUF_TYPE = new MediaType("application", "x-protobuf");

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return type == DiagnosticsResource.Diagnostics.class && mediaType.equals(DiagnosticsProtoMessageBodyReader.APPLICATION_X_PROTOBUF_TYPE);
  }

  @Override
  public DiagnosticsResource.Diagnostics readFrom(Class<DiagnosticsResource.Diagnostics> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

    DiagnosticsProtos.Diagnostics diagnosticsProto = DiagnosticsProtos.Diagnostics.parseFrom(entityStream);

    DiagnosticsResource.Diagnostics entity = new DiagnosticsResource.Diagnostics();
    entity.setId(diagnosticsProto.getId());
    entity.setTimestamp(diagnosticsProto.getTimestamp());
    entity.setSeverity(diagnosticsProto.getSeverity());
    entity.setTag(diagnosticsProto.getTag());
    entity.setInfo(diagnosticsProto.getInfo());

    return entity;
  }

}
