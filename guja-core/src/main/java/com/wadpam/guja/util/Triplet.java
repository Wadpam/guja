package com.wadpam.guja.util;

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

import java.io.Serializable;
import java.util.Objects;


/**
 * Triplet tuple implementation
 *
 * @author mattiaslevin
 * @author osandstrom
 */
public class Triplet<A, B, C> implements Serializable {

  private final A first;
  private final B second;
  private final C third;

  public static <A, B, C> Triplet<A, B, C> of(A first, B second, C third) {
    return new Triplet<>(first, second, third);
  }

  public static <A> Triplet<A, A, A> fromArray(A[] array) {
    return new Triplet<>(0 < array.length ? array[0] : null,
        1 < array.length ? array[1] : null,
        2 < array.length ? array[2] : null);
  }

  public Triplet(A first, B second, C third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Triplet) {
      Triplet other = (Triplet) o;
        return Objects.equals(this.first, other.first) &&
            Objects.equals(this.second, other.second) &&
            Objects.equals(this.third, other.third);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (31 * (31 * (null != first ? first.hashCode() : 0)) +
        (null != second ? second.hashCode() : 0)) +
        (null != third ? third.hashCode() : 0);
  }

  @Override
  public String toString() {
    return "Triple[" + first + ',' + second + ',' + third + ']';
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
