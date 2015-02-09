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
 * Resource with CRUD operations backed by a Dao.
 *
 * @author osandstrom
 *         Date: 1/19/14 Time: 10:54 AM
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(JsonCharacterEncodingResponseFilter.APPLICATION_JSON_UTF8)
public class CrudResource<T, ID extends Serializable, C extends Serializable, B extends DaoBuilder<T, ID, C>> {

  protected static final Logger LOGGER = LoggerFactory.getLogger(CrudResource.class);

  protected final B builder;

  public CrudResource(B builder) {
    this.builder = builder;
  }

  @GET
  @Path("count")
  public int count() throws IOException {
    return builder.count(null);
  }

  @POST
  @Transactional
  public Response create(T entity) throws URISyntaxException, IOException {
    final ID id = builder.put(null, null, entity);
    URI uri = new URI(id.toString());
    return Response.created(uri).entity(id).build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("id") ID id) throws IOException {
    checkNotNull(id);
    builder.delete(null, id);
    return Response.noContent().build();
  }

  @GET
  @Path("{id}")
  public Response read(@PathParam("id") ID id) throws IOException {
    checkNotNull(id);
    final T entity = builder.get(null, id);
    return null != entity ? Response.ok(entity).build() : Response.status(Response.Status.NOT_FOUND).build();
  }

  @GET
  public Response readPage(@QueryParam("pageSize") @DefaultValue("10") int pageSize,
                           @QueryParam("cursorKey") C cursorKey) {
    final QueryPage<T, C> page = builder.query().pageSize(pageSize).asPage(cursorKey);
    return Response.ok(page).build();
  }

  @POST
  @Path("{id}")
  public Response update(@PathParam("id") ID id, T entity) throws URISyntaxException, IOException {
    builder.put(null, id, entity);
    URI uri = new URI(id.toString());
    return Response.ok().contentLocation(uri).build();
  }


}
