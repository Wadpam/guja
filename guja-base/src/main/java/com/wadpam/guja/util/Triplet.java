package com.wadpam.guja.util;

/**
 * Triplet tuple implementation
 *
 * @author mattiaslevin
 */
public class Triplet<A, B, C> {

  private final A first;
  private final B second;
  private final C third;

  public static <A, B, C> Triplet<A, B, C> of(A first, B second, C third) {
    return new Triplet<>(first, second, third);
  }

  public Triplet(A first, B second, C third) {
    this.first = first;
    this.second = second;
    this.third = third;
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

}
