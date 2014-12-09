package com.wadpam.guja.readerwriter;

import com.wadpam.guja.filter.ProtoWrapperFilter;
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
@Produces("application/x-protobuf")
public class ResponseCodeProtoMessageBodyWriter implements MessageBodyWriter<ResponseCodeEntityWrapper> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseCodeProtoMessageBodyWriter.class);


  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    LOGGER.debug("Media type {} entity type {}", mediaType, genericType);
    return mediaType.equals(ProtoWrapperFilter.APPLICATION_X_PROTOBUF_TYPE) &&
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
