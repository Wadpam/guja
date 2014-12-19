package com.wadpam.guja.crud;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wadpam.guja.cache.LoadingCacheBuilder;
import net.sf.mardao.dao.AbstractDao;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    public CachedCrudResource(D dao, LoadingCacheBuilder cacheBuilder, long maximumSize) {
        super(dao);
        this.crudCacheLoader = new CacheLoader<ID, Response>() {
            @Override
            public Response load(ID id) throws Exception {
                return CachedCrudResource.super.read(id);
            }
        };
        this.crudCache = cacheBuilder.maximumSize(maximumSize).build(crudCacheLoader);
    }

    @Override
    public Response create(T entity) throws URISyntaxException, IOException {
        final Response response = super.create(entity);
        final ID id = (ID) response.getEntity();
        crudCache.put(id, Response.ok(entity).build());
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
        return response;
    }

    @Override
    public Response delete(@PathParam("id") ID id) throws IOException {
        final Response response = super.delete(id);
        crudCache.put(id, Response.status(Response.Status.NOT_FOUND).build());
        return response;
    }
}
