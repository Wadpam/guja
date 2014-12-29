package com.wadpam.guja.crud;

import com.google.common.collect.ImmutableList;
import com.wadpam.guja.cache.GuavaCacheBuilder;
import com.wadpam.guja.cache.CacheBuilder;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.AbstractDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by sosandstrom on 2014-12-19.
 */
public class CachedCrudResourceTest {
    AbstractDao<String, Long> daoMock;
    CachedCrudResource<String, Long, AbstractDao<String, Long>> resource;
    final CacheBuilder loadingCacheBuilder = new GuavaCacheBuilder();

    @Test
    public void testCrudCache() throws IOException, URISyntaxException {
        for (int i = 1; i < 50; i++) {
            expect(daoMock.get(Long.valueOf(i))).andReturn(Long.toString(i)).once();
        }

        replay(daoMock);

        for (int n = 0; n < 50; n++) {
            for (int i = 1; i < 50; i++) {

                Response actual = resource.read(Long.valueOf(i));
                assertEquals(Long.toString(i), actual.getEntity());
            }
        }
    }

    @Test
    public void testUpdateCache() throws IOException, URISyntaxException {

        for (int i = 1; i < 50; i++) {
            expect(daoMock.put("Updated" + (i - 1))).andReturn(Long.valueOf(i - 1)).once();
            expect(daoMock.get(Long.valueOf(i))).andReturn(Long.toString(i)).once();
        }
        expect(daoMock.put("Updated49")).andReturn(Long.valueOf(49)).once();

        replay(daoMock);

        for (int n = 0; n < 50; n++) {
            for (int i = 0; i < 50; i++) {

                // forced update
                if (i == n) {
                    resource.update(Long.valueOf(i), "Updated" + i);
                }

                Response actual = resource.read(Long.valueOf(i));

                assertEquals((i <= n ? "Updated" : "") + Long.toString(i), actual.getEntity());
            }
        }
    }

    @Test
    public void testDeleteCreateCache() throws IOException, URISyntaxException {

        for (int i = 0; i < 50; i++) {
            expect(daoMock.get(Long.valueOf(i))).andReturn(Long.toString(i)).once();
            daoMock.delete(Long.valueOf(i));
            expect(daoMock.put("Created" + i)).andReturn(Long.valueOf(i)).once();
        }

        replay(daoMock);

        for (int n = 0; n < 50; n++) {
            for (int i = 0; i < 50; i++) {

                // forced delete
                if (10 == n) {
                    resource.delete(Long.valueOf(i));
                }
                else if (20 == n) {
                    resource.create("Created" + i);
                }

                Response actual = resource.read(Long.valueOf(i));

                if (n < 10) {
                    assertEquals(Long.toString(i), actual.getEntity());
                }
                else if (n < 20) {
                    assertEquals(404, actual.getStatus());
                }
                else {
                    assertEquals("Created" + Long.toString(i), actual.getEntity());
                }
            }
        }
    }

    @Test
    public void testPageCache() {
        String cursorKey = null;
        for (int p = 3; p < 9; p++) {
            CursorPage<String> page = new CursorPage<>();
            page.setCursorKey("abc" + p);
            ImmutableList.Builder<String> builder = ImmutableList.builder();
            for (int i = 0; i < p; i++) {
                builder.add(Integer.toString(p));
            }
            page.setItems(builder.build());
            expect(daoMock.queryPage(p, cursorKey)).andReturn(page).once();
            cursorKey = page.getCursorKey();
        }

        replay(daoMock);

        for (int n = 0; n < 10; n++) {
            cursorKey = null;
            for (int p = 3; p < 9; p++) {
                Response response = resource.readPage(p, cursorKey);
                CursorPage<String> page = (CursorPage<String>) response.getEntity();
                cursorKey = page.getCursorKey();
                assertEquals(p, page.getItems().size());
                assertEquals(Integer.toString(p), page.getItems().iterator().next());
            }
        }
    }

    @Before
    public void setUp() {
        daoMock = createMock(AbstractDao.class);
        resource = new CachedCrudResource<>(daoMock, loadingCacheBuilder, 100, getClass().getName());
    }

    @After
    public void tearDown() {
        verify(daoMock);
    }
}
