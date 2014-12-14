package com.wadpam.guja.crud;

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
  public Response update(T entity) throws URISyntaxException, IOException {
    final ID id = dao.put(entity);
    URI uri = new URI(id.toString());
    return Response.ok().contentLocation(uri).build();
  }


}
