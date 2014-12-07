package com.wadpam.guja.util;

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
