package com.wadpam.guja.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TripletTest {


  @Test
  public void testFromArray() throws Exception {

    String[] array = new String[] {"first"};
    Triplet<String, String, String> triplet = Triplet.fromArray(array);
    assertTrue(triplet.first().equals("first"));
    assertTrue(null == triplet.second());
    assertTrue(null == triplet.third());

    array = new String[] {"first", "second"};
    triplet = Triplet.fromArray(array);
    assertTrue("first".equals(triplet.first()));
    assertTrue("second".equals(triplet.second()));
    assertTrue(null == triplet.third());

    array = new String[] {"first", "second", "third"};
    triplet = Triplet.fromArray(array);
    assertTrue("first".equals(triplet.first()));
    assertTrue("second".equals(triplet.second()));
    assertTrue("third".equals(triplet.third()));

    array[1] = null;
    triplet = Triplet.fromArray(array);
    assertTrue("first".equals(triplet.first()));
    assertTrue(null == triplet.second());
    assertTrue("third".equals(triplet.third()));

  }

  @Test
  public void testEquals() throws Exception {

    String[] array = new String[] {"first", "second", "third"};
    Triplet<String, String, String> triplet = Triplet.fromArray(array);

    String[] otherArray = new String[] {"first", "second", "third"};
    Triplet<String, String, String> otherTriplet = Triplet.fromArray(array);

    assertTrue(triplet.equals(otherTriplet));

    otherArray[0] = "other";
    assertFalse(triplet.equals(Triplet.fromArray(otherArray)));

    array[0] = null;
    otherArray[0] = null;
    assertTrue(Triplet.fromArray(array).equals(Triplet.fromArray(otherArray)));

    otherArray[1] = null;
    assertFalse(Triplet.fromArray(array).equals(Triplet.fromArray(otherArray)));

  }

}