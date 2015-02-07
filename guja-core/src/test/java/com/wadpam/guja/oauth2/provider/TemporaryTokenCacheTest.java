package com.wadpam.guja.oauth2.provider;

import com.google.common.base.Optional;
import com.wadpam.guja.cache.GuavaCacheBuilderProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TemporaryTokenCacheTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TemporaryTokenCacheTest.class);


  private static final String TOKEN = "some_token";
  private static final String KEY = "some_key";

  private TokenGenerator mockGenerator;
  private TemporaryTokenCache tokenCache;

  @Before
  public void setUp() throws Exception {
    mockGenerator = createMock(TokenGenerator.class);
    tokenCache = new TemporaryTokenCache(mockGenerator, new GuavaCacheBuilderProvider());
  }

  @After
  public void tearDown() throws Exception {
    verify(mockGenerator);
  }

  @Test
  public void testGenerateTemporaryToken() throws Exception {

    expect(mockGenerator.generate()).andReturn(TOKEN).once();
    replay(mockGenerator);

    String token = tokenCache.generateTemporaryToken(KEY, 10);
    assertTrue(null != token);
    assertTrue(TOKEN.equals(token));

  }

  @Test
  public void testValidateToken() throws Exception {

    expect(mockGenerator.generate()).andReturn(TOKEN).once();
    replay(mockGenerator);

    String token = tokenCache.generateTemporaryToken(KEY, 10);

    assertFalse(tokenCache.validateToken("wrong_key", TOKEN));
    assertFalse(tokenCache.validateToken(KEY, "wrong_token"));

    assertTrue(tokenCache.validateToken(KEY, TOKEN));
    assertFalse(tokenCache.validateToken(KEY, TOKEN));

  }

  @Test
  public void testGetContext() throws Exception {

    expect(mockGenerator.generate()).andReturn(TOKEN).once();
    replay(mockGenerator);

    String token = tokenCache.generateTemporaryToken(KEY, 10, "MyContext");

    assertFalse(tokenCache.validateToken("wrong_key", TOKEN));
    assertFalse(tokenCache.validateToken(KEY, "wrong_token"));

    Optional<Object> context = tokenCache.getContextForToken(KEY, TOKEN);
    assertTrue(context.isPresent());
    assertTrue(context.get().equals("MyContext"));
    assertFalse(tokenCache.validateToken(KEY, TOKEN));

  }

  @Test
  public void testGetMissingContext() throws Exception {

    expect(mockGenerator.generate()).andReturn(TOKEN).once();
    replay(mockGenerator);

    String token = tokenCache.generateTemporaryToken(KEY, 10);

    assertFalse(tokenCache.validateToken("wrong_key", TOKEN));
    assertFalse(tokenCache.validateToken(KEY, "wrong_token"));

    Optional<Object> context = tokenCache.getContextForToken(KEY, TOKEN);
    assertFalse(context.isPresent());
    assertFalse(tokenCache.validateToken(KEY, TOKEN));

  }

  @Test
  public void testRemoveToken() throws Exception {

    expect(mockGenerator.generate()).andReturn(TOKEN).once();
    replay(mockGenerator);

    String token = tokenCache.generateTemporaryToken(KEY, 10);

    tokenCache.removeToken(KEY);
    assertFalse(tokenCache.validateToken(KEY, TOKEN));

  }

}