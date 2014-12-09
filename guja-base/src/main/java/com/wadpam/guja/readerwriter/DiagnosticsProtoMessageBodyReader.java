package com.wadpam.guja.readerwriter;

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
