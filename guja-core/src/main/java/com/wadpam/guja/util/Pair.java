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

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Holds a Pair of objects.
 *
 * @author osandstrom Date: 2014-09-20 Time: 12:29
 */
public class Pair<M, N> implements Serializable, Comparable<Pair<M, N>> {
  private final M first;
  private final N second;

  public Pair(M first, N second) {
    this.first = first;
    this.second = second;
  }

  public static <K, V> Collection<Pair<K, V>> zip(Collection<K> firsts, Collection<V> seconds) {
    final Collection<Pair<K, V>> zipped = Lists.newArrayList();
    if (null != firsts && null != seconds) {
      Iterator<K> firstIterator = firsts.iterator();
      Iterator<V> secondIterator = seconds.iterator();
      while (firstIterator.hasNext() && secondIterator.hasNext()) {
        zipped.add(Pair.of(firstIterator.next(), secondIterator.next()));
      }
    }
    return zipped;
  }

  public static <K, V> Pair<Collection<K>, Collection<V>> unzip(Collection<Pair<K, V>> pairs) {
    final Collection<K> firsts = Lists.newArrayList();
    final Collection<V> seconds = Lists.newArrayList();
    if (null != pairs) {
      for (Pair<K, V> pair : pairs) {
        firsts.add(pair.first());
        seconds.add(pair.second);
      }
    }
    return Pair.of(firsts, seconds);
  }

  public static <K, V> Pair<K, V> of(K first, V second) {
    return new Pair<K, V>(first, second);
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

  @Override
  public String toString() {
    return (null != first ? first.toString() : "__null__") + "," + (null != second ? second.toString() : "__null__");
  }

  @Override
  public int compareTo(Pair<M, N> mnPair) {
    return toString().compareTo(mnPair.toString());
  }
}
