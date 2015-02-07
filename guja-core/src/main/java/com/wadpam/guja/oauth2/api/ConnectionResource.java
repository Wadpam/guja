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
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.domain.DConnection;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Path;
import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom
 *         Date: 1/18/14 Time: 8:07 PM
 */
@Path("_adm/connection")
@PermitAll // This resource is protected in web.xml. Must be GAE app developer to access this resource.
public class ConnectionResource extends CrudResource<DConnection, Long, String, DaoBuilder<DConnection, Long, String>> {

  @Inject
  public ConnectionResource(DConnectionDaoBean dao, DaoBuilderFactory builderFactory) {
    super(builderFactory.<DConnection, Long, String, Void, Serializable>create(dao, null));
  }

}
