package com.wadpam.guja.i18n.api;

/*
 * #%L
 * guja-base
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

import com.google.inject.Singleton;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.i18n.dao.Di18nDaoBean;
import com.wadpam.guja.i18n.domain.Di18n;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;


/**
 * Manage translations.
 *
 * @author mattiaslevin
 */
@Path("api/i18n")
@Singleton
@RolesAllowed({"ROLE_ADMIN"})
public class I18nResource extends CrudResource<Di18n, Long, Di18nDaoBean> {

  public I18nResource(Di18nDaoBean dao) {
    super(dao);
  }

}
