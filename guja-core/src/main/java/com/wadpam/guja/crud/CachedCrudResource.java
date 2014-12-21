package com.wadpam.guja.crud;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wadpam.guja.cache.LoadingCacheBuilder;
import com.wadpam.guja.cache.PageCacheKey;
import net.sf.mardao.dao.AbstractDao;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * Created by sosandstrom on 2014-12-18.
 */
public class CachedCrudResource<T, ID extends Serializable, D extends AbstractDao<T, ID>> extends CrudResource<T, ID, D> {

    protected final LoadingCache<ID, Response> crudCache;
    private final CacheLoader<ID, Response> crudCacheLoader;
    protected final LoadingCache<PageCacheKey, Response> pageCache;
    private final CacheLoader<PageCacheKey, Response> pageCacheLoader;

    public CachedCrudResource(D dao, LoadingCacheBuilder cacheBuilder, long maximumSize) {
        super(dao);

        this.crudCacheLoader = new CacheLoader<ID, Response>() {
            @Override
            public Response load(ID id) throws Exception {
                return CachedCrudResource.super.read(id);
            }
        };
        this.crudCache = cacheBuilder.from("").maximumSize(maximumSize).build(crudCacheLoader);

        this.pageCacheLoader = new CacheLoader<PageCacheKey, Response>() {
            @Override
            public Response load(PageCacheKey pageCacheKey) throws Exception {
                return CachedCrudResource.super.readPage(pageCacheKey.pageSize, pageCacheKey.cursorKey);
            }
        };
        this.pageCache = cacheBuilder.from("").maximumSize(maximumSize).build(pageCacheLoader);
    }

    @Override
    public Response create(T entity) throws URISyntaxException, IOException {
        final Response response = super.create(entity);
        final ID id = (ID) response.getEntity();
        crudCache.put(id, Response.ok(entity).build());
        pageCache.invalidateAll();
        return response;
    }

    @GET
    @Path("{id}")
    @Override
    public Response read(@PathParam("id") ID id) throws IOException {
        final Response response = crudCache.getUnchecked(id);
        return response;
    }

    @POST
    @Path("{id}")
    @Override
    public Response update(@PathParam("id") ID id, T entity) throws URISyntaxException, IOException {
        final Response response = super.update(id, entity);
        crudCache.put(id, Response.ok(entity).build());
        pageCache.invalidateAll();
        return response;
    }

    @Override
    public Response delete(@PathParam("id") ID id) throws IOException {
        final Response response = super.delete(id);
        crudCache.put(id, Response.status(Response.Status.NOT_FOUND).build());
        pageCache.invalidateAll();
        return response;
    }

    @GET
    @Override
    public Response readPage(@QueryParam("pageSize") @DefaultValue("10") int pageSize, @QueryParam("cursorKey") String cursorKey) {
        return pageCache.getUnchecked(new PageCacheKey(pageSize, cursorKey));
    }
}
