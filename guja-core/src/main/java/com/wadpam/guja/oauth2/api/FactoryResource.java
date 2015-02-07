/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wadpam.guja.oauth2.api;

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

import com.google.inject.Inject;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.dao.DaoBuilder;
import com.wadpam.guja.dao.DaoBuilderFactory;
import com.wadpam.guja.oauth2.dao.DFactoryDaoBean;
import com.wadpam.guja.oauth2.domain.DFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Path;
import java.io.Serializable;

/**
 * @author osandstrom
 */
@Path("_adm/factory")
@PermitAll // This resource is protected in web.xml. Must be GAE app developer to access this resource.
public class FactoryResource extends CrudResource<DFactory, String, String, DaoBuilder<DFactory, String, String>> {

  public static final String PROVIDER_ID_FACEBOOK = "facebook";
  public static final String PROVIDER_ID_SELF = "self";

  @Inject
  public FactoryResource(DFactoryDaoBean dao, DaoBuilderFactory builderFactory) {
    super(builderFactory.<DFactory, String, String, Void, Serializable>create(dao, null));
  }


}
