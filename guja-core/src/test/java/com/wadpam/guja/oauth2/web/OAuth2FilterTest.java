package com.wadpam.guja.oauth2.web;

import com.google.inject.Provider;
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.dao.DOAuth2UserDaoBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by sosandstrom on 2015-01-16.
 */
public class OAuth2FilterTest {

    OAuth2Filter filter;
    final ProviderReference<DConnectionDaoBean> connectionDaoProvider = new ProviderReference<>();
    final ProviderReference<DOAuth2UserDaoBean> userDaoProvider = new ProviderReference<>();
    DConnectionDaoBean connectionDaoMock;
    DOAuth2UserDaoBean userDaoMock;
    private FilterChain chainMock;
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Before
    public void setUp() {
        connectionDaoMock = createMock(DConnectionDaoBean.class);
        connectionDaoProvider.set(connectionDaoMock);
        userDaoMock = createMock(DOAuth2UserDaoBean.class);
        userDaoProvider.set(userDaoMock);
        filter = new OAuth2Filter(connectionDaoProvider);
        chainMock = createMock(FilterChain.class);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testExpiredToken() throws IOException, ServletException {
        request.setParameter("access_token", "abc123");
        expect(connectionDaoMock.findByAccessToken("abc123")).andReturn(null).once();
        replayAll();

        filter.doFilter(request, response, chainMock);

        assertEquals(401, response.getStatus());
        assertEquals("Bearer error=\"invalid_token\"", response.getHeader("WWW-Authenticate"));
    }

    private void replayAll() {
        replay(connectionDaoMock, userDaoMock, chainMock);
    }

    @After
    public void tearDown() {
        verify(connectionDaoMock, userDaoMock, chainMock);
    }

    static class ProviderReference<T> extends AtomicReference<T> implements Provider<T> {

    }
}
