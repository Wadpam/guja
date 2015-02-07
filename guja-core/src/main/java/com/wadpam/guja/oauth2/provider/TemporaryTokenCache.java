package com.wadpam.guja.oauth2.provider;


import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.inject.Inject;
import com.wadpam.guja.cache.CacheBuilderProvider;
import com.wadpam.guja.util.Pair;
import com.wadpam.guja.util.Triplet;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generate short lived tokens stored in a cache.
 *
 * @author mattiaslevin
 */
public class TemporaryTokenCache {
  private static final Logger LOGGER = LoggerFactory.getLogger(TemporaryTokenCache.class);

  private static final int DEFAULT_TOKEN_DURATION_SECONDS = 60 * 60; // 60 minutes
  private final TokenGenerator tokenGenerator;
  private final Cache<String, Triplet<String, Long, Object>> tokenCache; // key, timeToLiveSeconds, context

  @Inject
  public TemporaryTokenCache(TokenGenerator tokenGenerator, CacheBuilderProvider cacheBuilderProvider) {
    this.tokenGenerator = tokenGenerator;
    
    // TODO How to handle generics
    this.tokenCache =  cacheBuilderProvider.get()
        .expireAfterWrite(DEFAULT_TOKEN_DURATION_SECONDS)
        .name(this.getClass().getName())
        .build();

  }

  /**
   * Generate a short lived token and store in the cache.
   * @param key cache key
   * @param secondsToLive number of seconds the token is valid
   * @return the newly generated temporary token
   */
  public String generateTemporaryToken(String key, int secondsToLive) {
    return generateTemporaryToken(key, secondsToLive, null);
  }

  /**
   * Same as above but will also store a context that can be retried later.
   */
  public String generateTemporaryToken(String key, int secondsToLive, Object context) {
    checkNotNull(key);
    String token = tokenGenerator.generate();
    tokenCache.put(key, Triplet.of(token, DateTime.now().plusSeconds(secondsToLive).getMillis(), context));
    return token;
  }


  /**
   * Validate a short lived token by looking in the cache and checking the time to live.
   * The token is removed if found and valid.
   * @param key cacheKey
   * @param token temporary token to validate
   * @return true of the token is still valid
   */
  public boolean validateToken(String key, String token) {
    return validateTokenAndGetContext(key, token).first();
  }

  private Pair<Boolean, Object> validateTokenAndGetContext(String key, String token) {
    checkNotNull(key);
    checkNotNull(token);

    Triplet<String, Long, Object> storedToken = tokenCache.getIfPresent(key);
    boolean isValid = null != storedToken &&
        token.equals(storedToken.first()) &&
        new DateTime(storedToken.second()).isAfterNow();

    if (isValid) {
      tokenCache.invalidate(key);
    }

    return Pair.of(isValid, isValid ? storedToken.third() : null);
  }

  /**
   * Validate a short lived token and returning the associated context
   * The token is removed if found and valid.
   * @param key cacheKey
   * @param token temporary token to validate
   * @return an optional. The context will be absent if either the token is invalid of the context is null.
   */
  public Optional<Object> getContextForToken(String key, String token) {
    Object context = validateTokenAndGetContext(key, token).second();
    return null != context ? Optional.of(context) : Optional.absent();
  }

  /**
   * Remove a token from the cache.
   * @param key key
   */
  public void removeToken(String key) {
    tokenCache.invalidate(checkNotNull(key));
  }

}
