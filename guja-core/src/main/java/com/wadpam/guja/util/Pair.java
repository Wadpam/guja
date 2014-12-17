package com.wadpam.guja.util;

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

import java.io.Serializable;

/**
 * Holds a Pair of objects.
 *
 * @author osandstrom Date: 2014-09-20 Time: 12:29
 */
public class Pair<M, N> implements Serializable {
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
