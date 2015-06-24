package com.wadpam.guja;

/*
 * #%L
 * guja-base
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

import com.google.common.collect.ImmutableMap;
import com.wadpam.guja.api.SemanticVersionCheckPredicate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SemanticVersionCheckPredicateTest {

  private SemanticVersionCheckPredicate predicate;

  @Before
  public void setUp() throws Exception {

    predicate = new SemanticVersionCheckPredicate("ios:1.0.0,android:>=1.5");

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testIsVersionSupported() throws Exception {

    assertTrue(predicate.isVersionSupported("ios", "1.0.0"));
    assertFalse(predicate.isVersionSupported("ios", "0.9.0"));
    assertFalse(predicate.isVersionSupported("ios", "1.0.3"));

    assertTrue(predicate.isVersionSupported("android", "1.5.1"));
    assertFalse(predicate.isVersionSupported("android", "1.4.0"));

  }

}