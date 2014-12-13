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

import java.util.Date;

/**
 * By default, the rank is set at the time the document is created to the number of seconds since January 1, 2011.
 *
 * @author osandstrom Date: 2014-09-20 Time: 16:27
 */
public class Rank {
  public static final long MILLIS_RANK_EPOCH = 1293840000000L;

  public static int getCurrentRank() {
    return getRank(System.currentTimeMillis());
  }

  public static int getRank(long millis) {
    return (int) ((millis - MILLIS_RANK_EPOCH) / 1000L);
  }

  public static Date getDate(int rank) {
    return new Date(MILLIS_RANK_EPOCH + (1000 * rank));
  }

  public static void main(String args[]) {
    System.out.println("Millis epoch: " + MILLIS_RANK_EPOCH);
    System.out.println("Current rank: " + getCurrentRank());
    System.out.println("Date epoch: " + getDate(0));
  }
}
