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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Map this Resource with a
 *
 * @author osandstrom
 * @Path("path/{parentId}/path").
 */
@Consumes(value = {MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON)
public class ParentedCrudResource<PT, PID extends Serializable, P extends AbstractDao<PT, PID>, T, ID extends Serializable,
    D extends AbstractDao<T, ID>> {

  protected static final Logger LOGGER = LoggerFactory.getLogger(CrudResource.class);
  protected final D dao;
  protected final P parentDao;

  public ParentedCrudResource(P parentDao, D dao) {
    this.parentDao = parentDao;
    this.dao = dao;
  }

  @POST
  @Transactional
  public Response create(@PathParam("parentId") PID parentId, T entity) throws URISyntaxException, IOException {
    checkNotNull(parentId);

    // Objects such as parentKey cannot be properly JSONed:
    final Object parentKey = parentDao.getKey(parentId);
    //dao.setParentKey(entity, parentKey);

    final ID id = dao.put(entity);
    URI uri = new URI(id.toString());
    return Response.created(uri).entity(id).build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("parentId") PID parentId,
                         @PathParam("id") ID id) throws IOException {
    checkNotNull(parentId);
    checkNotNull(id);

    final Object parentKey = parentDao.getKey(null, parentId);
    dao.delete(parentKey, id);

    return Response.noContent().build();
  }

  @GET
  @Path("{id}")
  public Response read(@PathParam("parentId") PID parentId,
                       @PathParam("id") ID id) throws IOException {
    checkNotNull(parentId);
    checkNotNull(id);

    final Object parentKey = parentDao.getKey(null, parentId);
    final T entity = dao.get(parentKey, id);
    if (null == entity) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(entity).build();
  }

  @GET
  public Response readPage(@PathParam("parentId") PID parentId,
                           @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                           @QueryParam("cursorKey") String cursorKey) {
    checkNotNull(parentId);

    final Object parentKey = parentDao.getKey(null, parentId);
    final CursorPage<T> page = dao.queryPage(parentKey, pageSize, cursorKey);
    return Response.ok(page).build();
  }

  @POST
  @Path("{id}")
  public Response update(@PathParam("parentId") PID parentId,
                         @PathParam("id") ID id, T entity) throws URISyntaxException, IOException {
    checkNotNull(parentId);
    checkNotNull(id);

    // Objects such as parentKey cannot be properly JSONed:
    final Object parentKey = parentDao.getKey(null, parentId);
    dao.setParentKey(entity, parentKey);

    final ID eId = (ID) dao.getId(entity);
    if (!id.equals(eId)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    dao.put(entity);
    URI uri = new URI(id.toString());
    return Response.ok().contentLocation(uri).build();
  }

}
