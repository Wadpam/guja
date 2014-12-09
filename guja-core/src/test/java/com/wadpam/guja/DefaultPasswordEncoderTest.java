package com.wadpam.guja;

import com.wadpam.guja.oauth2.providers.DefaultPasswordEncoder;
import com.wadpam.guja.oauth2.providers.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class DefaultPasswordEncoderTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPasswordEncoderTest.class);


  private PasswordEncoder encoder;

  @org.junit.Before
  public void setUp() throws Exception {
    encoder = new DefaultPasswordEncoder();
  }

  @org.junit.After
  public void tearDown() throws Exception {
  }

  @org.junit.Test
  public void testEncode() throws Exception {

    String encodedPassword = encoder.encode("Password01");
    LOGGER.debug("Password {}", encodedPassword);

    // TODO Make sure the encoder always give back the same string. LOGGER is not working
  }

  @org.junit.Test
  public void testMatches() throws Exception {

    String encodedPassword = encoder.encode("Password01");
    assertTrue(encoder.matches("Password01", encodedPassword));

  }

}