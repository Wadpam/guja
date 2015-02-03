package com.wadpam.guja.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Deserialize dates in UNIX timestamp, seconds since EPOC
 * @author mattiaslevin
 */
public class DateDeserializer extends StdDeserializer<Date> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DateDeserializer.class);


  public DateDeserializer(Class<?> vc) {
    super(vc);
  }

  public DateDeserializer(JavaType valueType) {
    super(valueType);
  }

  @Override
  public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    Long timestamp = jp.readValueAs(Long.class);
    return null != timestamp ? new Date(timestamp * 1000) : null;
  }

}
