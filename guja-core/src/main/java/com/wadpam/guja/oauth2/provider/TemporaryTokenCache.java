package com.wadpam.guja.oauth2.provider;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.inject.Inject;
import com.wadpam.guja.util.Pair;


import javax.cache.Cache;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generate short lived tokens stored in a cache.
 *
 * @author mattiaslevin
 */
public class TemporaryTokenCache {

  private final Cache cache;
  private final TokenGenerator tokenGenerator;

  @Inject
  public TemporaryTokenCache(Cache cache, TokenGenerator tokenGenerator) {
    this.cache = cache;
    this.tokenGenerator = tokenGenerator;
  }

  /**
   * Generate a short lived token and store in the cache.
   * @param key cache key
   * @param timeToLive number of millis the token is valid
   * @return the newly generated temporary token
   */
  public String generateTemporaryToken(String key, int timeToLive) {
    checkNotNull(key);
    String token = tokenGenerator.generate();
    cache.put(key, Pair.of(token, DateTime.now().plusMillis(timeToLive).getMillis()));
    return token;
  }


  /**
   * Validate a short lived token by looking in the cache and checking the time to live.
   * @param key cacheKey
   * @param token temporary token to validate
   * @return true of the token is still valid
   */
  public boolean validateToken(String key, String token) {
    checkNotNull(key);
    checkNotNull(token);
    Pair<String, Long> storedToken = (Pair<String, Long>)cache.get(key);
      return null != storedToken &&
          token.equals(storedToken.first()) &&
          new DateTime(storedToken.getSecond()).isAfterNow();
  }

  /**
   * Remove a token from the cache.
   * @param key
   */
  public void removeToken(String key) {
    cache.remove(checkNotNull(key));
  }

}
