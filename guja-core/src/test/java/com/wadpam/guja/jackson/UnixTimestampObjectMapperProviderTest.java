package com.wadpam.guja.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.junit.Assert.*;

public class UnixTimestampObjectMapperProviderTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(UnixTimestampObjectMapperProviderTest.class);
  private static final long ALLOWED_DIFFERENCE = 10 * 1000;

  private final long timestamp = 1422985778000L;
  private final String message = "Test message";
  private final Date date = new Date(timestamp);

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testUnixTimestampSerialize() throws Exception {

    UnixTimestampObjectMapperProvider provider = new UnixTimestampObjectMapperProvider();
    ObjectMapper mapper = provider.get();

    ObjectWriter writer = mapper.writer();
    String jsonString = writer.writeValueAsString(new StupidPojo(message, date));
    LOGGER.info(jsonString);

    assertTrue(jsonString.contains(String.format("%s", timestamp / 1000)));
    assertTrue(!jsonString.contains(String.format("%s", timestamp)));

  }

  @Test
  public void testUnixTimestampDeserialize() throws Exception {

    UnixTimestampObjectMapperProvider provider = new UnixTimestampObjectMapperProvider();
    ObjectMapper mapper = provider.get();

    ObjectReader reader = mapper.reader();
    StupidPojo pojo = reader
        .withType(StupidPojo.class)
        .readValue(String.format("{\"message\" : \"Test message\", \"date\":%s}", timestamp / 1000));

    LOGGER.info("{}", pojo);

    DateTime refDate = new DateTime(date);
    Interval interval = getInterval(new DateTime(pojo.getDate()), refDate);
    assertTrue(Math.abs(interval.toDurationMillis()) < ALLOWED_DIFFERENCE);

  }

  private static Interval getInterval(DateTime date, DateTime otherDate) {
    return date.isBefore(otherDate) ?
        new Interval(date, otherDate) :
        new Interval(otherDate, date);
  }


  public static class StupidPojo {

    private String message;
    private Date date;

    public StupidPojo() {
    }

    public StupidPojo(String message, Date date) {
      this.message = message;
      this.date = date;
    }

    @Override
    public String toString() {
      return String.format("%s : %s", message, date);
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }
  }

}