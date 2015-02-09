package com.wadpam.guja.crud;

/*
 * #%L
 * guja-core
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.inject.persist.Transactional;
import com.wadpam.guja.dao.DaoBuilder;
import com.wadpam.guja.dao.ParentDaoBuilder;
import com.wadpam.guja.dao.QueryPage;
import com.wadpam.guja.web.JsonCharacterEncodingResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Map this Resource with a
 *
 * @author osandstrom
 * @Path("path/{parentId}/path").
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(JsonCharacterEncodingResponseFilter.APPLICATION_JSON_UTF8)
public class ParentedCrudResource<C extends Serializable, PT, PID extends Serializable, P extends ParentDaoBuilder<Void, Serializable, C, PT, PID>,
        T, ID extends Serializable, B extends DaoBuilder<T, ID, C>> {

  protected static final Logger LOGGER = LoggerFactory.getLogger(CrudResource.class);
  protected final B builder;
  protected final P parentBuilder;

  public ParentedCrudResource(P parentBuilder, B builder) {
    this.parentBuilder = parentBuilder;
    this.builder = builder;
  }

  @POST
  @Transactional
  public Response create(@PathParam("parentId") PID parentId, T entity) throws URISyntaxException, IOException {
    checkNotNull(parentId);

    // Objects such as parentKey cannot be properly JSONed:
    final Object parentKey = parentBuilder.getKey(parentId);
    builder.setParentKey(entity, parentKey);

    final ID id = builder.put(parentKey, null, entity);
    URI uri = new URI(id.toString());
    return Response.created(uri).entity(id).build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("parentId") PID parentId,
                         @PathParam("id") ID id) throws IOException {
    checkNotNull(parentId);
    checkNotNull(id);

    final Object parentKey = parentBuilder.getKey(parentId);
    builder.delete(parentKey, id);

    return Response.noContent().build();
  }

  @GET
  @Path("{id}")
  public Response read(@PathParam("parentId") PID parentId,
                       @PathParam("id") ID id) throws IOException {
    checkNotNull(parentId);
    checkNotNull(id);

    final Object parentKey = parentBuilder.getKey(parentId);
    final T entity = builder.get(parentKey, id);
    if (null == entity) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(entity).build();
  }

  @GET
  public Response readPage(@PathParam("parentId") PID parentId,
                           @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                           @QueryParam("cursorKey") C cursorKey) {
    checkNotNull(parentId);

    final Object parentKey = parentBuilder.getKey(parentId);
    final QueryPage<T, C> page = builder.query().parent(parentKey).pageSize(pageSize).asPage(cursorKey);
    return Response.ok(page).build();
  }

  @POST
  @Path("{id}")
  public Response update(@PathParam("parentId") PID parentId,
                         @PathParam("id") ID id, T entity) throws URISyntaxException, IOException {
    checkNotNull(parentId);
    checkNotNull(id);

    // Objects such as parentKey cannot be properly JSONed:
    final Object parentKey = parentBuilder.getKey(parentId);
    builder.setParentKey(entity, parentKey);

    builder.put(parentKey, id, entity);
    URI uri = new URI(id.toString());
    return Response.ok().contentLocation(uri).build();
  }

}
