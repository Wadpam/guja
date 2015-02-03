package com.wadpam.guja.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;
import java.util.Date;

/**
 * A true Unix timestamp serializer/deserializer, seconds since EPOC.
 *
 * @author mattiaslevin
 */
@Provider
@Singleton
public class UnixTimestampObjectMapperProvider extends NonNullObjectMapperProvider {

  @Override
  void configureMapper(final ObjectMapper mapper) {
    super.configureMapper(mapper);

    SimpleModule module = new SimpleModule();
    module.addSerializer(new DateSerializer(Date.class));
    module.addDeserializer(Date.class, new DateDeserializer(Date.class));
    mapper.registerModule(module);

  }

}
