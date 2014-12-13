package com.wadpam.guja.oauth2.dao;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.core.filter.Filter;
import net.sf.mardao.core.geo.DLocation;
import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Supplier;
import com.wadpam.guja.oauth2.domain.DFactory;

/**
 * The DFactory domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2014-12-03T00:02:58.463+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDFactoryDaoImpl
  extends AbstractDao<DFactory, java.lang.String> {

  public GeneratedDFactoryDaoImpl(Supplier supplier) {
    super(new DFactoryMapper(supplier), supplier);
  }

// ----------------------- field finders -------------------------------
  /**
   * query-by method for field baseUrl
   * @param baseUrl the specified attribute
   * @return an Iterable of DFactorys for the specified baseUrl
   */
  public Iterable<DFactory> queryByBaseUrl(java.lang.String baseUrl) {
    return queryByField(null, DFactoryMapper.Field.BASEURL.getFieldName(), baseUrl);
  }

  /**
   * query-by method for field clientId
   * @param clientId the specified attribute
   * @return an Iterable of DFactorys for the specified clientId
   */
  public Iterable<DFactory> queryByClientId(java.lang.String clientId) {
    return queryByField(null, DFactoryMapper.Field.CLIENTID.getFieldName(), clientId);
  }

  /**
   * query-by method for field clientSecret
   * @param clientSecret the specified attribute
   * @return an Iterable of DFactorys for the specified clientSecret
   */
  public Iterable<DFactory> queryByClientSecret(java.lang.String clientSecret) {
    return queryByField(null, DFactoryMapper.Field.CLIENTSECRET.getFieldName(), clientSecret);
  }

  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of DFactorys for the specified createdBy
   */
  public Iterable<DFactory> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DFactoryMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of DFactorys for the specified createdDate
   */
  public Iterable<DFactory> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DFactoryMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of DFactorys for the specified updatedBy
   */
  public Iterable<DFactory> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DFactoryMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of DFactorys for the specified updatedDate
   */
  public Iterable<DFactory> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DFactoryMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }


// ----------------------- query methods -------------------------------

  public CursorPage<DFactory> queryPage(int requestedPageSize, String cursorString) {
    return queryPage(false, requestedPageSize, null,
      null, false, null, false, null, cursorString);
  }
}
