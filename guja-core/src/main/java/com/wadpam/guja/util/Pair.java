package com.wadpam.guja.util;

/**
 * Holds a Pair of objects.
 *
 * @author osandstrom Date: 2014-09-20 Time: 12:29
 */
public class Pair<M, N> {
  private final M first;
  private final N second;

  public Pair(M first, N second) {
    this.first = first;
    this.second = second;
  }

  public static <K, V> Pair<K, V> of(K first, V second) {
    return new Pair(first, second);
  }

  public M first() {
    return first;
  }

  public N second() {
    return second;
  }

  public M getFirst() {
    return first;
  }

  public N getSecond() {
    return second;
  }
}
