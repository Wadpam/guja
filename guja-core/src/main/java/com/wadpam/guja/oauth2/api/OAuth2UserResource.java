package com.wadpam.guja.oauth2.api;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.oauth2.dao.DOAuth2UserDaoBean;
import com.wadpam.guja.oauth2.domain.DOAuth2User;
import com.wadpam.guja.oauth2.web.OAuth2Filter;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;


/**
 * Manage an oauth2 user.
 *
 * @author osandstrom
 * @author mattiaslevin
 */
@Path("api/oauth2user")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"ROLE_ADMIN"})
public class OAuth2UserResource extends CrudResource<DOAuth2User, Long, DOAuth2UserDaoBean> {

  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

  public static final Collection<String> DEFAULT_ROLES_USER = Lists.newArrayList(ROLE_USER);
  public static final Collection<String> DEFAULT_ROLES_ADMIN = Lists.newArrayList(ROLE_ADMIN, ROLE_USER);


  @Inject
  public OAuth2UserResource(DOAuth2UserDaoBean dao) {
    super(dao);
  }

  @GET
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response readMe(@Context HttpServletRequest request) throws IOException {
    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    return read(id);
  }

  @POST
  @Path("me")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  public Response updateMe(@Context HttpServletRequest request, DOAuth2User entity)
      throws URISyntaxException, IOException {
    Long id = (Long) request.getAttribute(OAuth2Filter.NAME_USER_ID);
    if (null == entity.getId()) {
      entity.setId(id);
    } else if (!entity.getId().equals(id)) {
      throw new BadRequestRestException("User ids does not match");
    }
    return update(entity);
  }


}
