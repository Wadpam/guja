package com.wadpam.guja.crud;

import com.google.common.base.Optional;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wadpam.guja.cache.LoadingCacheBuilder;
import com.wadpam.guja.cache.PageCacheKey;
import net.sf.mardao.core.CursorPage;
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

    protected final LoadingCache<ID, Optional<T>> crudCache;
    private final CacheLoader<ID, Optional<T>> crudCacheLoader;
    protected final LoadingCache<PageCacheKey, CursorPage<T>> pageCache;
    private final CacheLoader<PageCacheKey, CursorPage<T>> pageCacheLoader;

    public CachedCrudResource(D dao, LoadingCacheBuilder cacheBuilder, long maximumSize, String namespace) {
        super(dao);

        this.crudCacheLoader = new CacheLoader<ID, Optional<T>>() {
            @Override
            public Optional<T> load(ID id) throws Exception {
                final T entity = (T) CachedCrudResource.super.read(id).getEntity();
                return null != entity ? Optional.of(entity) : Optional.<T>absent();
            }
        };
        this.crudCache = cacheBuilder.from(namespace).maximumSize(maximumSize).build(crudCacheLoader);

        this.pageCacheLoader = new CacheLoader<PageCacheKey, CursorPage<T>>() {
            @Override
            public CursorPage<T> load(PageCacheKey pageCacheKey) throws Exception {
                return (CursorPage<T>) CachedCrudResource.super.readPage(pageCacheKey.pageSize, pageCacheKey.cursorKey).getEntity();
            }
        };
        this.pageCache = cacheBuilder.from(namespace).expireAfterWrite(5*60).build(pageCacheLoader);
    }

    @Override
    public Response create(T entity) throws URISyntaxException, IOException {
        final Response response = super.create(entity);
        final ID id = (ID) response.getEntity();
        crudCache.put(id, Optional.of(entity));
        return response;
    }

    @GET
    @Path("{id}")
    @Override
    public Response read(@PathParam("id") ID id) throws IOException {
        final Optional<T> entity = crudCache.getUnchecked(id);
        final Response response = null != entity && entity.isPresent() ?
                Response.ok(entity.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
        return response;
    }

    @POST
    @Path("{id}")
    @Override
    public Response update(@PathParam("id") ID id, T entity) throws URISyntaxException, IOException {
        final Response response = super.update(id, entity);
        crudCache.put(id, Optional.of(entity));
        return response;
    }

    @Override
    public Response delete(@PathParam("id") ID id) throws IOException {
        final Response response = super.delete(id);
        crudCache.put(id, Optional.<T>absent());
        return response;
    }

    @GET
    @Override
    public Response readPage(@QueryParam("pageSize") @DefaultValue("10") int pageSize, @QueryParam("cursorKey") String cursorKey) {
        CursorPage<T> page = pageCache.getUnchecked(new PageCacheKey(pageSize, cursorKey));
        return Response.ok(page).build();
    }
}
