package com.wadpam.guja.api;

import com.google.inject.Inject;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.dao.DContactDaoBean;
import com.wadpam.guja.domain.DContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Manage contacts.
 *
 * A contacts my have a parent-child relationship, e.g. to create organisations or hierarchies.
 *
 * @author mattiaslevin
 */
@Path("api/contact")
@PermitAll
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class ContactResource extends CrudResource<DContact, Long, DContactDaoBean> {
  protected static final Logger LOGGER = LoggerFactory.getLogger(ContactResource.class);


  @Inject
  public ContactResource(DContactDaoBean dao) {
    super(dao);
  }

  /**
   * Find a contact based on its unique tag.
   *
   * @param uniqueTag unique contacts tag
   * @return a contact entity
   */
  @GET
  @Path("uniquetag/{uniqueTag}")
  public Response findByUniqueTag(@PathParam("uniqueTag") String uniqueTag) {
    checkNotNull(uniqueTag);

    DContact contact = dao.findByUniqueTag(null, uniqueTag);

    return null != contact ? Response.ok(contact).build() : Response.status(Response.Status.NOT_FOUND).build();
  }

//  /**
//   * Find all contacts matching a specific tag.
//   *
//   * @param tags tag to match against
//   * @param pageSize page size. Optional.
//   * @param cursorKey cursor key. Optional.
//   * @return a page of contact entities
//   */
//  @GET
//  @Path("tags")
//  public Response queryByTags(@QueryParam("tag") Collection<String> tags, // TODO WIll not work. How to inject an array params with same name?
//                              @QueryParam("pageSize") @DefaultValue("10") int pageSize,
//                              @QueryParam("cursorKey") String cursorKey) {
//    checkNotNull(tags);
//
//    CursorPage<DContact> page = dao.queryPageByTags(null, tags, pageSize, cursorKey);
//
//    return Response.ok(page).build();
//  }

}
