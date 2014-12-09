package com.wadpam.guja.api;

import com.google.inject.Singleton;
import com.wadpam.guja.crud.CrudResource;
import com.wadpam.guja.dao.Di18nDaoBean;
import com.wadpam.guja.domain.Di18n;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;


/**
 * Manage translations
 * @author mattiaslevin
 */
@Path("api/i18n")
@Singleton
@RolesAllowed("{ROLE_ADMIN}")
public class I18nResource extends CrudResource<Di18n, Long, Di18nDaoBean> {

    public I18nResource(Di18nDaoBean dao) {
        super(dao);
    }

}
