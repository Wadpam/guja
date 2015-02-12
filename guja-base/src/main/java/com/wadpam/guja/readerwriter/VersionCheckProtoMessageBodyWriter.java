package com.wadpam.guja.readerwriter;

import com.wadpam.guja.api.VersionCheckResource;
import com.wadpam.guja.filter.ProtoWrapperResponseFilter;
import com.wadpam.guja.proto.Protos;
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
 * Serialize a version check response into protobuffer representation.
 *
 * @author mattiaslevin
 */
@Provider
@Singleton
@Produces(ProtoWrapperResponseFilter.APPLICATION_X_PROTOBUF)
public class VersionCheckProtoMessageBodyWriter implements MessageBodyWriter<VersionCheckResource.VersionCheckResponse> {
  private static final Logger LOGGER = LoggerFactory.getLogger(VersionCheckProtoMessageBodyWriter.class);

  @Override
  public boolean isWriteable(Class<?> aClass, Type genericType, Annotation[] annotations, MediaType mediaType) {
    LOGGER.debug("Media type {} entity type {}", mediaType, genericType);
    return mediaType.equals(ProtoWrapperResponseFilter.APPLICATION_X_PROTOBUF_TYPE) &&
        genericType == VersionCheckResource.VersionCheckResponse.class;
  }

  @Override
  public long getSize(VersionCheckResource.VersionCheckResponse box, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
    // Not used since Jersey 2.0
    // Ignore
    return 0;
  }

  @Override
  public void writeTo(VersionCheckResource.VersionCheckResponse response, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {
    LOGGER.debug("Serialize to protocol buffer");

    Protos.VersionCheck.Builder builder = Protos.VersionCheck.newBuilder();

    if (null != response.getUrl()) {
      builder.setUrl(response.getUrl());
    }

    builder.build().writeTo(outputStream);

  }

}
