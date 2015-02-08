package com.wadpam.guja.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Quartet tuple implementation.
 *
 * @author mattiaslevin
 */
public class Quartet<A, B, C, D> implements Serializable {

  private final A first;
  private final B second;
  private final C third;
  private final D fourth;

  public static <A, B, C, D> Quartet<A, B, C, D> of(A first, B second, C third, D fourth) {
    return new Quartet<>(first, second, third, fourth);
  }

  public static <A> Quartet<A, A, A, A> fromArray(A[] array) {
    return new Quartet<>(
        0 < array.length ? array[0] : null,
        1 < array.length ? array[1] : null,
        2 < array.length ? array[2] : null,
        3 < array.length ? array[3] : null);
  }

  public Quartet(A first, B second, C third, D fourth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Quartet) {
      Quartet other = (Quartet) o;
      return Objects.equals(this.first, other.first) &&
          Objects.equals(this.second, other.second) &&
          Objects.equals(this.third, other.third) &&
          Objects.equals(this.fourth, other.fourth);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (31 * (31 * (null != first ? first.hashCode() : 0)) +
        (null != second ? second.hashCode() : 0)) +
        (null != third ? third.hashCode() : 0) +
        (null != fourth ? fourth.hashCode() : 0);
  }

  @Override
  public String toString() {
    return "Quartet[" + first + ',' + second + ',' + third + ',' + fourth + ']';
  }

  public A first() {
    return first;
  }

  public B second() {
    return second;
  }

  public C third() {
    return third;
  }

  public D fourth() {
    return fourth;
  }

}
