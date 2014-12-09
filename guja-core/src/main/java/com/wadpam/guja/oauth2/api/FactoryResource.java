/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wadpam.guja.oauth2.api;

import com.google.inject.Inject;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.oauth2.dao.DFactoryDaoBean;
import com.wadpam.guja.oauth2.domain.DFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author osandstrom
 */
@Path("_adm/factories")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll // This resource is protected in web.xml. Must be GAE app developer to access this resource.
public class FactoryResource extends CrudResource<DFactory, String, DFactoryDaoBean> {

  public static final String PROVIDER_ID_FACEBOOK = "facebook";
  public static final String PROVIDER_ID_SELF = "self";

  @Inject
  public FactoryResource(DFactoryDaoBean dao) {
    super(dao);
  }


}
