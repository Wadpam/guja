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
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.dao.AbstractDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Resource with CRUD operations backed by a Dao.
 *
 * @author osandstrom
 *         Date: 1/19/14 Time: 10:54 AM
 */
@Consumes(value = {MediaType.APPLICATION_JSON})
@Produces(CrudResource.MIME_JSON_UTF8)
public class CrudResource<T, ID extends Serializable, D extends AbstractDao<T, ID>> {
  public static final String MIME_JSON_UTF8 = "application/json; charset=UTF-8";

  protected static final Logger LOGGER = LoggerFactory.getLogger(CrudResource.class);

  protected final D dao;

  public CrudResource(D dao) {
    this.dao = dao;
  }

  @GET
  @Path("count")
  public int count() {
    return dao.count();
  }

  @POST
  @Transactional
  public Response create(T entity) throws URISyntaxException, IOException {
    final ID id = dao.put(entity);
    URI uri = new URI(id.toString());
    return Response.created(uri).entity(id).build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("id") ID id) throws IOException {
    dao.delete(id);

    return Response.noContent().build();
  }

  @GET
  @Path("{id}")
  public Response read(@PathParam("id") ID id) throws IOException {
    final T entity = dao.get(id);

    if (null == entity) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(entity).build();
  }

  @GET
  public Response readPage(@QueryParam("pageSize") @DefaultValue("10") int pageSize,
                           @QueryParam("cursorKey") String cursorKey) {
    final CursorPage<T> page = dao.queryPage(pageSize, cursorKey);
    return Response.ok(page).build();
  }

  @POST
  @Path("{id}")
  public Response update(@PathParam("id") ID id, T entity) throws URISyntaxException, IOException {
    dao.put(entity);
    URI uri = new URI(id.toString());
    return Response.ok().contentLocation(uri).build();
  }


}
