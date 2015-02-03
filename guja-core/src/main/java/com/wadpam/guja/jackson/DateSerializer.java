package com.wadpam.guja.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Serialize dates in UNIX timestamp, seconds since EPOC
 * @author mattiaslevin
 */
public class DateSerializer extends StdSerializer<Date> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DateSerializer.class);


  public DateSerializer(Class<Date> t) {
    super(t);
  }

  public DateSerializer(JavaType type) {
    super(type);
  }

  public DateSerializer(Class<?> t, boolean dummy) {
    super(t, dummy);
  }

  @Override
  public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
    LOGGER.debug("Serialize {}", date);
    jsonGenerator.writeNumber((date == null) ? 0L : date.getTime() / 1000);
  }

}
