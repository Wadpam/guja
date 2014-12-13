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
import com.wadpam.guja.oauth2.domain.DOAuth2User;

/**
 * The DOAuth2User domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2014-12-03T00:02:58.463+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDOAuth2UserDaoImpl
  extends AbstractDao<DOAuth2User, java.lang.Long> {

  public GeneratedDOAuth2UserDaoImpl(Supplier supplier) {
    super(new DOAuth2UserMapper(supplier), supplier);
  }

// ----------------------- field finders -------------------------------
  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of DOAuth2Users for the specified createdBy
   */
  public Iterable<DOAuth2User> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DOAuth2UserMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of DOAuth2Users for the specified createdDate
   */
  public Iterable<DOAuth2User> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DOAuth2UserMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field displayName
   * @param displayName the specified attribute
   * @return an Iterable of DOAuth2Users for the specified displayName
   */
  public Iterable<DOAuth2User> queryByDisplayName(java.lang.String displayName) {
    return queryByField(null, DOAuth2UserMapper.Field.DISPLAYNAME.getFieldName(), displayName);
  }

  /**
   * query-by method for field email
   * @param email the specified attribute
   * @return an Iterable of DOAuth2Users for the specified email
   */
  public Iterable<DOAuth2User> queryByEmail(java.lang.String email) {
    return queryByField(null, DOAuth2UserMapper.Field.EMAIL.getFieldName(), email);
  }

  /**
   * query-by method for field profileLink
   * @param profileLink the specified attribute
   * @return an Iterable of DOAuth2Users for the specified profileLink
   */
  public Iterable<DOAuth2User> queryByProfileLink(java.lang.String profileLink) {
    return queryByField(null, DOAuth2UserMapper.Field.PROFILELINK.getFieldName(), profileLink);
  }

  /**
   * query-by method for field roles
   * @param roles the specified attribute
   * @return an Iterable of DOAuth2Users for the specified roles
   */
  public Iterable<DOAuth2User> queryByRoles(java.lang.Object roles) {
    return queryByField(null, DOAuth2UserMapper.Field.ROLES.getFieldName(), roles);
  }

  /**
   * query-by method for field state
   * @param state the specified attribute
   * @return an Iterable of DOAuth2Users for the specified state
   */
  public Iterable<DOAuth2User> queryByState(java.lang.Integer state) {
    return queryByField(null, DOAuth2UserMapper.Field.STATE.getFieldName(), state);
  }

  /**
   * query-by method for field thumbnailUrl
   * @param thumbnailUrl the specified attribute
   * @return an Iterable of DOAuth2Users for the specified thumbnailUrl
   */
  public Iterable<DOAuth2User> queryByThumbnailUrl(java.lang.String thumbnailUrl) {
    return queryByField(null, DOAuth2UserMapper.Field.THUMBNAILURL.getFieldName(), thumbnailUrl);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of DOAuth2Users for the specified updatedBy
   */
  public Iterable<DOAuth2User> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DOAuth2UserMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of DOAuth2Users for the specified updatedDate
   */
  public Iterable<DOAuth2User> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DOAuth2UserMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }


// ----------------------- query methods -------------------------------

  public CursorPage<DOAuth2User> queryPage(int requestedPageSize, String cursorString) {
    return queryPage(false, requestedPageSize, null,
      null, false, null, false, null, cursorString);
  }
}
