package com.wadpam.guja.oauth2.provider;


import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.common.cache.Cache;
import com.google.inject.Inject;
import com.wadpam.guja.cache.CacheBuilderProvider;
import com.wadpam.guja.util.Pair;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generate short lived tokens stored in a cache.
 *
 * @author mattiaslevin
 */
public class TemporaryTokenCache {

  private static final int DEFAULT_TOKEN_DURATION_SECONDS = 60 * 60; // 60 minutes
  private final TokenGenerator tokenGenerator;
  private final Cache<String, Pair<String, Long>> tokenCache;

  @Inject
  public TemporaryTokenCache(TokenGenerator tokenGenerator, CacheBuilderProvider cacheBuilderProvider) {
    this.tokenGenerator = tokenGenerator;
    
    // TODO How to handle generics
    this.tokenCache =  cacheBuilderProvider.get()
        .expireAfterWrite(DEFAULT_TOKEN_DURATION_SECONDS)
        .from(this.getClass().getName())
        .build();

  }

  /**
   * Generate a short lived token and store in the cache.
   * @param key cache key
   * @param secondsToLive number of seconds the token is valid
   * @return the newly generated temporary token
   */
  public String generateTemporaryToken(String key, int secondsToLive) {
    checkNotNull(key);
    String token = tokenGenerator.generate();
    tokenCache.put(key, Pair.of(token, DateTime.now().plusSeconds(secondsToLive).getMillis()));
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
    Pair<String, Long> storedToken = tokenCache.getIfPresent(key);
    if (null != storedToken) {
      tokenCache.invalidate(key);
    }
    return null != storedToken &&
        token.equals(storedToken.first()) &&
        new DateTime(storedToken.getSecond()).isAfterNow();
  }

  /**
   * Remove a token from the cache.
   * @param key
   */
  public void removeToken(String key) {
    tokenCache.invalidate(checkNotNull(key));
  }

}
