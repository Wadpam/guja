package com.wadpam.guja;

/*
 * #%L
 * guja-core
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
    assertTrue(encoder.matches("Password01", encodedPassword));

  }

  @org.junit.Test
  public void testMatches() throws Exception {

    String encodedPassword = encoder.encode("Password01");
    assertTrue(encoder.matches("Password01", "1000:283754265ae0b0ac9a9cdf2cec645c6247ac5ad1aca878a8:a22a4f1dd54c366fab33278f313db46b36d9905facf65948"));

  }

}