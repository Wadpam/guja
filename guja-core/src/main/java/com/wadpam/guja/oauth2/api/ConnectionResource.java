package com.wadpam.guja.oauth2.api;

import com.google.inject.Inject;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.domain.DConnection;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom
 * Date: 1/18/14 Time: 8:07 PM
 */
@Path("_adm/connections")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll // This resource is protected in web.xml. Must be GAE app developer to access this resource.
public class ConnectionResource extends CrudResource<DConnection, Long, DConnectionDaoBean> {

  @Inject
  public ConnectionResource(DConnectionDaoBean dao) {
    super(dao);
  }

}
