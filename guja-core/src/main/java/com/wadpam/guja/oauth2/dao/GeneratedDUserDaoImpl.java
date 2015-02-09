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
import com.wadpam.guja.oauth2.domain.DUser;
import net.sf.mardao.core.Cached;
import net.sf.mardao.core.CacheConfig;
import java.io.IOException;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheValue;


/**
 * The DUser domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2015-02-07T18:50:32.440+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDUserDaoImpl
  extends AbstractDao<DUser, java.lang.Long> {

  public GeneratedDUserDaoImpl(Supplier supplier) {
    super(new DUserMapper(supplier), supplier);
  }

// ----------------------- Caching -------------------------------------

  // Cache crud methods
  @CacheResult
  @Override
  public DUser get(@CacheKey Object parentKey, @CacheKey java.lang.Long id) throws IOException {
    return super.get(parentKey, id);
  }

  @CachePut
  @Override
  public java.lang.Long put(@CacheKey Object parentKey, @CacheKey java.lang.Long id, @CacheValue DUser entity) throws IOException {
    return super.put(parentKey, id, entity);
  }

  @CacheRemove
  @Override
  public void delete(@CacheKey Object parentKey, @CacheKey java.lang.Long id) throws IOException {
    super.delete(parentKey, id);
  }


// ----------------------- field finders -------------------------------
  /**
   * query-by method for field address1
   * @param address1 the specified attribute
   * @return an Iterable of DUsers for the specified address1
   */
  public Iterable<DUser> queryByAddress1(java.lang.String address1) {
    return queryByField(null, DUserMapper.Field.ADDRESS1.getFieldName(), address1);
  }

  /**
   * query-by method for field address2
   * @param address2 the specified attribute
   * @return an Iterable of DUsers for the specified address2
   */
  public Iterable<DUser> queryByAddress2(java.lang.String address2) {
    return queryByField(null, DUserMapper.Field.ADDRESS2.getFieldName(), address2);
  }

  /**
   * query-by method for field birthInfo
   * @param birthInfo the specified attribute
   * @return an Iterable of DUsers for the specified birthInfo
   */
  public Iterable<DUser> queryByBirthInfo(java.lang.String birthInfo) {
    return queryByField(null, DUserMapper.Field.BIRTHINFO.getFieldName(), birthInfo);
  }

  /**
   * query-by method for field city
   * @param city the specified attribute
   * @return an Iterable of DUsers for the specified city
   */
  public Iterable<DUser> queryByCity(java.lang.String city) {
    return queryByField(null, DUserMapper.Field.CITY.getFieldName(), city);
  }

  /**
   * query-by method for field country
   * @param country the specified attribute
   * @return an Iterable of DUsers for the specified country
   */
  public Iterable<DUser> queryByCountry(java.lang.String country) {
    return queryByField(null, DUserMapper.Field.COUNTRY.getFieldName(), country);
  }

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
   * find-by method for unique field email
   * @param email the unique attribute
   * @return the unique DUser for the specified email
   */
  public DUser findByEmail(java.lang.String email) {
    return queryUniqueByField(null, DUserMapper.Field.EMAIL.getFieldName(), email);
  }

  /**
   * query-by method for field firstName
   * @param firstName the specified attribute
   * @return an Iterable of DUsers for the specified firstName
   */
  public Iterable<DUser> queryByFirstName(java.lang.String firstName) {
    return queryByField(null, DUserMapper.Field.FIRSTNAME.getFieldName(), firstName);
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
   * query-by method for field lastName
   * @param lastName the specified attribute
   * @return an Iterable of DUsers for the specified lastName
   */
  public Iterable<DUser> queryByLastName(java.lang.String lastName) {
    return queryByField(null, DUserMapper.Field.LASTNAME.getFieldName(), lastName);
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
   * query-by method for field phoneNumber1
   * @param phoneNumber1 the specified attribute
   * @return an Iterable of DUsers for the specified phoneNumber1
   */
  public Iterable<DUser> queryByPhoneNumber1(java.lang.String phoneNumber1) {
    return queryByField(null, DUserMapper.Field.PHONENUMBER1.getFieldName(), phoneNumber1);
  }

  /**
   * query-by method for field phoneNumber2
   * @param phoneNumber2 the specified attribute
   * @return an Iterable of DUsers for the specified phoneNumber2
   */
  public Iterable<DUser> queryByPhoneNumber2(java.lang.String phoneNumber2) {
    return queryByField(null, DUserMapper.Field.PHONENUMBER2.getFieldName(), phoneNumber2);
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

  /**
   * query-by method for field zipCode
   * @param zipCode the specified attribute
   * @return an Iterable of DUsers for the specified zipCode
   */
  public Iterable<DUser> queryByZipCode(java.lang.String zipCode) {
    return queryByField(null, DUserMapper.Field.ZIPCODE.getFieldName(), zipCode);
  }


// ----------------------- DUser builder -------------------------------

  public static DUserMapper.Builder newBuilder() {
    return DUserMapper.newBuilder();
  }

}
