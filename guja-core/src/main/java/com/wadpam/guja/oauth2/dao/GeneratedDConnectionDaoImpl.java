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
import com.wadpam.guja.oauth2.domain.DConnection;

/**
 * The DConnection domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2014-12-03T00:02:58.463+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDConnectionDaoImpl
  extends AbstractDao<DConnection, java.lang.Long> {

  public GeneratedDConnectionDaoImpl(Supplier supplier) {
    super(new DConnectionMapper(supplier), supplier);
  }

// ----------------------- field finders -------------------------------
  /**
   * find-by method for unique field accessToken
   * @param accessToken the unique attribute
   * @return the unique DConnection for the specified accessToken
   */
  public DConnection findByAccessToken(java.lang.String accessToken) {
    return queryUniqueByField(null, DConnectionMapper.Field.ACCESSTOKEN.getFieldName(), accessToken);
  }

  /**
   * query-by method for field appArg0
   * @param appArg0 the specified attribute
   * @return an Iterable of DConnections for the specified appArg0
   */
  public Iterable<DConnection> queryByAppArg0(java.lang.String appArg0) {
    return queryByField(null, DConnectionMapper.Field.APPARG0.getFieldName(), appArg0);
  }

  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of DConnections for the specified createdBy
   */
  public Iterable<DConnection> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DConnectionMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of DConnections for the specified createdDate
   */
  public Iterable<DConnection> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DConnectionMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field displayName
   * @param displayName the specified attribute
   * @return an Iterable of DConnections for the specified displayName
   */
  public Iterable<DConnection> queryByDisplayName(java.lang.String displayName) {
    return queryByField(null, DConnectionMapper.Field.DISPLAYNAME.getFieldName(), displayName);
  }

  /**
   * query-by method for field expireTime
   * @param expireTime the specified attribute
   * @return an Iterable of DConnections for the specified expireTime
   */
  public Iterable<DConnection> queryByExpireTime(java.util.Date expireTime) {
    return queryByField(null, DConnectionMapper.Field.EXPIRETIME.getFieldName(), expireTime);
  }

  /**
   * query-by method for field imageUrl
   * @param imageUrl the specified attribute
   * @return an Iterable of DConnections for the specified imageUrl
   */
  public Iterable<DConnection> queryByImageUrl(java.lang.String imageUrl) {
    return queryByField(null, DConnectionMapper.Field.IMAGEURL.getFieldName(), imageUrl);
  }

  /**
   * query-by method for field profileUrl
   * @param profileUrl the specified attribute
   * @return an Iterable of DConnections for the specified profileUrl
   */
  public Iterable<DConnection> queryByProfileUrl(java.lang.String profileUrl) {
    return queryByField(null, DConnectionMapper.Field.PROFILEURL.getFieldName(), profileUrl);
  }

  /**
   * query-by method for field providerId
   * @param providerId the specified attribute
   * @return an Iterable of DConnections for the specified providerId
   */
  public Iterable<DConnection> queryByProviderId(java.lang.String providerId) {
    return queryByField(null, DConnectionMapper.Field.PROVIDERID.getFieldName(), providerId);
  }

  /**
   * query-by method for field providerUserId
   * @param providerUserId the specified attribute
   * @return an Iterable of DConnections for the specified providerUserId
   */
  public Iterable<DConnection> queryByProviderUserId(java.lang.String providerUserId) {
    return queryByField(null, DConnectionMapper.Field.PROVIDERUSERID.getFieldName(), providerUserId);
  }

  /**
   * find-by method for unique field refreshToken
   * @param refreshToken the unique attribute
   * @return the unique DConnection for the specified refreshToken
   */
  public DConnection findByRefreshToken(java.lang.String refreshToken) {
    return queryUniqueByField(null, DConnectionMapper.Field.REFRESHTOKEN.getFieldName(), refreshToken);
  }

  /**
   * query-by method for field secret
   * @param secret the specified attribute
   * @return an Iterable of DConnections for the specified secret
   */
  public Iterable<DConnection> queryBySecret(java.lang.String secret) {
    return queryByField(null, DConnectionMapper.Field.SECRET.getFieldName(), secret);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of DConnections for the specified updatedBy
   */
  public Iterable<DConnection> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DConnectionMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of DConnections for the specified updatedDate
   */
  public Iterable<DConnection> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DConnectionMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }

  /**
   * query-by method for field userId
   * @param userId the specified attribute
   * @return an Iterable of DConnections for the specified userId
   */
  public Iterable<DConnection> queryByUserId(java.lang.Long userId) {
    return queryByField(null, DConnectionMapper.Field.USERID.getFieldName(), userId);
  }

  /**
   * query-by method for field userRoles
   * @param userRoles the specified attribute
   * @return an Iterable of DConnections for the specified userRoles
   */
  public Iterable<DConnection> queryByUserRoles(java.lang.String userRoles) {
    return queryByField(null, DConnectionMapper.Field.USERROLES.getFieldName(), userRoles);
  }


// ----------------------- query methods -------------------------------

  public CursorPage<DConnection> queryPage(int requestedPageSize, String cursorString) {
    return queryPage(false, requestedPageSize, null,
      null, false, null, false, null, cursorString);
  }
}
