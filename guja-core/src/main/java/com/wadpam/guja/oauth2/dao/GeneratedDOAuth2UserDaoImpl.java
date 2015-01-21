package com.wadpam.guja.oauth2.dao;

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
import net.sf.mardao.core.Cached;
import net.sf.mardao.core.CacheConfig;
import java.io.IOException;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheValue;


/**
 * The DOAuth2User domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2015-01-20T22:12:36.907+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDOAuth2UserDaoImpl
  extends AbstractDao<DOAuth2User, java.lang.Long> {

  public GeneratedDOAuth2UserDaoImpl(Supplier supplier) {
    super(new DOAuth2UserMapper(supplier), supplier);
  }

// ----------------------- Caching -------------------------------------

  // Cache crud methods
  @CacheResult
  @Override
  public DOAuth2User get(@CacheKey Object parentKey, @CacheKey java.lang.Long id) throws IOException {
    return super.get(parentKey, id);
  }

  @CachePut
  @Override
  public java.lang.Long put(@CacheKey Object parentKey, @CacheKey java.lang.Long id, @CacheValue DOAuth2User entity) throws IOException {
    return super.put(parentKey, id, entity);
  }

  @CacheRemove
  @Override
  public void delete(@CacheKey Object parentKey, @CacheKey java.lang.Long id) throws IOException {
    super.delete(parentKey, id);
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
   * find-by method for unique field email
   * @param email the unique attribute
   * @return the unique DOAuth2User for the specified email
   */
  public DOAuth2User findByEmail(java.lang.String email) {
    return queryUniqueByField(null, DOAuth2UserMapper.Field.EMAIL.getFieldName(), email);
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


// ----------------------- DOAuth2User builder -------------------------------

  public static DOAuth2UserMapper.Builder newBuilder() {
    return DOAuth2UserMapper.newBuilder();
  }

}
