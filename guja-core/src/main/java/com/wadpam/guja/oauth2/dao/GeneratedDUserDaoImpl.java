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
import com.wadpam.guja.oauth2.domain.DUser;

/**
 * The DUser domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2014-12-14T21:20:34.275+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDUserDaoImpl
  extends AbstractDao<DUser, java.lang.Long> {

  public GeneratedDUserDaoImpl(Supplier supplier) {
    super(new DUserMapper(supplier), supplier);
  }

// ----------------------- field finders -------------------------------
  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of DUsers for the specified createdBy
   */
  public Iterable<DUser> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DUserMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of DUsers for the specified createdDate
   */
  public Iterable<DUser> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DUserMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field displayName
   * @param displayName the specified attribute
   * @return an Iterable of DUsers for the specified displayName
   */
  public Iterable<DUser> queryByDisplayName(java.lang.String displayName) {
    return queryByField(null, DUserMapper.Field.DISPLAYNAME.getFieldName(), displayName);
  }

  /**
   * query-by method for field email
   * @param email the specified attribute
   * @return an Iterable of DUsers for the specified email
   */
  public Iterable<DUser> queryByEmail(java.lang.String email) {
    return queryByField(null, DUserMapper.Field.EMAIL.getFieldName(), email);
  }

  /**
   * query-by method for field friends
   * @param friends the specified attribute
   * @return an Iterable of DUsers for the specified friends
   */
  public Iterable<DUser> queryByFriends(java.lang.Object friends) {
    return queryByField(null, DUserMapper.Field.FRIENDS.getFieldName(), friends);
  }

  /**
   * query-by method for field password
   * @param password the specified attribute
   * @return an Iterable of DUsers for the specified password
   */
  public Iterable<DUser> queryByPassword(java.lang.String password) {
    return queryByField(null, DUserMapper.Field.PASSWORD.getFieldName(), password);
  }

  /**
   * query-by method for field profileLink
   * @param profileLink the specified attribute
   * @return an Iterable of DUsers for the specified profileLink
   */
  public Iterable<DUser> queryByProfileLink(java.lang.String profileLink) {
    return queryByField(null, DUserMapper.Field.PROFILELINK.getFieldName(), profileLink);
  }

  /**
   * query-by method for field roles
   * @param roles the specified attribute
   * @return an Iterable of DUsers for the specified roles
   */
  public Iterable<DUser> queryByRoles(java.lang.Object roles) {
    return queryByField(null, DUserMapper.Field.ROLES.getFieldName(), roles);
  }

  /**
   * query-by method for field state
   * @param state the specified attribute
   * @return an Iterable of DUsers for the specified state
   */
  public Iterable<DUser> queryByState(java.lang.Integer state) {
    return queryByField(null, DUserMapper.Field.STATE.getFieldName(), state);
  }

  /**
   * query-by method for field thumbnailUrl
   * @param thumbnailUrl the specified attribute
   * @return an Iterable of DUsers for the specified thumbnailUrl
   */
  public Iterable<DUser> queryByThumbnailUrl(java.lang.String thumbnailUrl) {
    return queryByField(null, DUserMapper.Field.THUMBNAILURL.getFieldName(), thumbnailUrl);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of DUsers for the specified updatedBy
   */
  public Iterable<DUser> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DUserMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of DUsers for the specified updatedDate
   */
  public Iterable<DUser> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DUserMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }

  /**
   * find-by method for unique field username
   * @param username the unique attribute
   * @return the unique DUser for the specified username
   */
  public DUser findByUsername(java.lang.String username) {
    return queryUniqueByField(null, DUserMapper.Field.USERNAME.getFieldName(), username);
  }


// ----------------------- query methods -------------------------------


}
