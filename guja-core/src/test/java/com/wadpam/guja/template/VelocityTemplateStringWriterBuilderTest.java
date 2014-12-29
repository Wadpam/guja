package com.wadpam.guja.template;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Locale;

import static org.junit.Assert.*;

public class VelocityTemplateStringWriterBuilderTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(VelocityTemplateStringWriterBuilderTest.class);


  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testEnLocale() throws Exception {

    StringWriter writer = VelocityTemplateStringWriterBuilder.withTemplate("hello.vm")
        .locale(Locale.forLanguageTag("en"))
        .put("name", "Mattias")
        .build();

    assertTrue("Hello Mattias".equals(writer.toString()));

  }

  @Test
  public void testSvLocale() throws Exception {

    StringWriter writer = VelocityTemplateStringWriterBuilder.withTemplate("hello.vm")
        .locale(Locale.forLanguageTag("sv"))
        .put("name", "Mattias")
        .build();

    LOGGER.info("{}", writer.toString());
    assertTrue("Hejsan Mattias".equals(writer.toString()));

  }

  // TODO More tests

}