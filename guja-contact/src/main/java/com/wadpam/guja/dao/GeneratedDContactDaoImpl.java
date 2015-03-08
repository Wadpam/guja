package com.wadpam.guja.dao;

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
import com.wadpam.guja.domain.DContact;
import net.sf.mardao.core.Cached;
import net.sf.mardao.core.CacheConfig;
import java.io.IOException;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheValue;


/**
 * The DContact domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2015-03-08T21:44:48.699+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDContactDaoImpl
  extends AbstractDao<DContact, java.lang.Long> {

  public GeneratedDContactDaoImpl(Supplier<Object, Object, Object, Object> supplier) {
    super(new DContactMapper(supplier), supplier);
  }

// ----------------------- Caching -------------------------------------

  // Cache crud methods
  @CacheResult
  @Override
  public DContact get(@CacheKey Object parentKey, @CacheKey java.lang.Long id) throws IOException {
    return super.get(parentKey, id);
  }

  @CachePut
  @Override
  public java.lang.Long put(@CacheKey Object parentKey, @CacheKey java.lang.Long id, @CacheValue DContact entity) throws IOException {
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
   * @return an Iterable of DContacts for the specified address1
   */
  public Iterable<DContact> queryByAddress1(Object parent, java.lang.String address1) {
    return queryByField(parent, DContactMapper.Field.ADDRESS1.getFieldName(), address1);
  }

  /**
   * query-by method for field address2
   * @param address2 the specified attribute
   * @return an Iterable of DContacts for the specified address2
   */
  public Iterable<DContact> queryByAddress2(Object parent, java.lang.String address2) {
    return queryByField(parent, DContactMapper.Field.ADDRESS2.getFieldName(), address2);
  }

  /**
   * query-by method for field appArg0
   * @param appArg0 the specified attribute
   * @return an Iterable of DContacts for the specified appArg0
   */
  public Iterable<DContact> queryByAppArg0(Object parent, java.lang.Long appArg0) {
    return queryByField(parent, DContactMapper.Field.APPARG0.getFieldName(), appArg0);
  }

  /**
   * query-by method for field birthday
   * @param birthday the specified attribute
   * @return an Iterable of DContacts for the specified birthday
   */
  public Iterable<DContact> queryByBirthday(Object parent, java.util.Date birthday) {
    return queryByField(parent, DContactMapper.Field.BIRTHDAY.getFieldName(), birthday);
  }

  /**
   * query-by method for field city
   * @param city the specified attribute
   * @return an Iterable of DContacts for the specified city
   */
  public Iterable<DContact> queryByCity(Object parent, java.lang.String city) {
    return queryByField(parent, DContactMapper.Field.CITY.getFieldName(), city);
  }

  /**
   * query-by method for field companyName
   * @param companyName the specified attribute
   * @return an Iterable of DContacts for the specified companyName
   */
  public Iterable<DContact> queryByCompanyName(Object parent, java.lang.String companyName) {
    return queryByField(parent, DContactMapper.Field.COMPANYNAME.getFieldName(), companyName);
  }

  /**
   * query-by method for field country
   * @param country the specified attribute
   * @return an Iterable of DContacts for the specified country
   */
  public Iterable<DContact> queryByCountry(Object parent, java.lang.String country) {
    return queryByField(parent, DContactMapper.Field.COUNTRY.getFieldName(), country);
  }

  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of DContacts for the specified createdBy
   */
  public Iterable<DContact> queryByCreatedBy(Object parent, java.lang.String createdBy) {
    return queryByField(parent, DContactMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of DContacts for the specified createdDate
   */
  public Iterable<DContact> queryByCreatedDate(Object parent, java.util.Date createdDate) {
    return queryByField(parent, DContactMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field email
   * @param email the specified attribute
   * @return an Iterable of DContacts for the specified email
   */
  public Iterable<DContact> queryByEmail(Object parent, java.lang.String email) {
    return queryByField(parent, DContactMapper.Field.EMAIL.getFieldName(), email);
  }

  /**
   * query-by method for field facebook
   * @param facebook the specified attribute
   * @return an Iterable of DContacts for the specified facebook
   */
  public Iterable<DContact> queryByFacebook(Object parent, java.lang.String facebook) {
    return queryByField(parent, DContactMapper.Field.FACEBOOK.getFieldName(), facebook);
  }

  /**
   * query-by method for field firstName
   * @param firstName the specified attribute
   * @return an Iterable of DContacts for the specified firstName
   */
  public Iterable<DContact> queryByFirstName(Object parent, java.lang.String firstName) {
    return queryByField(parent, DContactMapper.Field.FIRSTNAME.getFieldName(), firstName);
  }

  /**
   * query-by method for field homePhone
   * @param homePhone the specified attribute
   * @return an Iterable of DContacts for the specified homePhone
   */
  public Iterable<DContact> queryByHomePhone(Object parent, java.lang.String homePhone) {
    return queryByField(parent, DContactMapper.Field.HOMEPHONE.getFieldName(), homePhone);
  }

  /**
   * query-by method for field lastName
   * @param lastName the specified attribute
   * @return an Iterable of DContacts for the specified lastName
   */
  public Iterable<DContact> queryByLastName(Object parent, java.lang.String lastName) {
    return queryByField(parent, DContactMapper.Field.LASTNAME.getFieldName(), lastName);
  }

  /**
   * query-by method for field latitude
   * @param latitude the specified attribute
   * @return an Iterable of DContacts for the specified latitude
   */
  public Iterable<DContact> queryByLatitude(Object parent, java.lang.Float latitude) {
    return queryByField(parent, DContactMapper.Field.LATITUDE.getFieldName(), latitude);
  }

  /**
   * query-by method for field linkedIn
   * @param linkedIn the specified attribute
   * @return an Iterable of DContacts for the specified linkedIn
   */
  public Iterable<DContact> queryByLinkedIn(Object parent, java.lang.String linkedIn) {
    return queryByField(parent, DContactMapper.Field.LINKEDIN.getFieldName(), linkedIn);
  }

  /**
   * query-by method for field longitude
   * @param longitude the specified attribute
   * @return an Iterable of DContacts for the specified longitude
   */
  public Iterable<DContact> queryByLongitude(Object parent, java.lang.Float longitude) {
    return queryByField(parent, DContactMapper.Field.LONGITUDE.getFieldName(), longitude);
  }

  /**
   * query-by method for field mobilePhone
   * @param mobilePhone the specified attribute
   * @return an Iterable of DContacts for the specified mobilePhone
   */
  public Iterable<DContact> queryByMobilePhone(Object parent, java.lang.String mobilePhone) {
    return queryByField(parent, DContactMapper.Field.MOBILEPHONE.getFieldName(), mobilePhone);
  }

  /**
   * query-by method for field otherEmail
   * @param otherEmail the specified attribute
   * @return an Iterable of DContacts for the specified otherEmail
   */
  public Iterable<DContact> queryByOtherEmail(Object parent, java.lang.String otherEmail) {
    return queryByField(parent, DContactMapper.Field.OTHEREMAIL.getFieldName(), otherEmail);
  }

  /**
   * query-by method for field otherPhone
   * @param otherPhone the specified attribute
   * @return an Iterable of DContacts for the specified otherPhone
   */
  public Iterable<DContact> queryByOtherPhone(Object parent, java.lang.String otherPhone) {
    return queryByField(parent, DContactMapper.Field.OTHERPHONE.getFieldName(), otherPhone);
  }

  /**
   * query-by method for field primaryCustomIndex
   * @param primaryCustomIndex the specified attribute
   * @return an Iterable of DContacts for the specified primaryCustomIndex
   */
  public Iterable<DContact> queryByPrimaryCustomIndex(Object parent, java.lang.String primaryCustomIndex) {
    return queryByField(parent, DContactMapper.Field.PRIMARYCUSTOMINDEX.getFieldName(), primaryCustomIndex);
  }

  /**
   * query-by method for field secondaryCustomIndex
   * @param secondaryCustomIndex the specified attribute
   * @return an Iterable of DContacts for the specified secondaryCustomIndex
   */
  public Iterable<DContact> queryBySecondaryCustomIndex(Object parent, java.lang.String secondaryCustomIndex) {
    return queryByField(parent, DContactMapper.Field.SECONDARYCUSTOMINDEX.getFieldName(), secondaryCustomIndex);
  }

  /**
   * query-by method for field tags
   * @param tags the specified attribute
   * @return an Iterable of DContacts for the specified tags
   */
  public Iterable<DContact> queryByTags(Object parent, java.lang.Object tags) {
    return queryByField(parent, DContactMapper.Field.TAGS.getFieldName(), tags);
  }

  /**
   * query-by method for field twitter
   * @param twitter the specified attribute
   * @return an Iterable of DContacts for the specified twitter
   */
  public Iterable<DContact> queryByTwitter(Object parent, java.lang.String twitter) {
    return queryByField(parent, DContactMapper.Field.TWITTER.getFieldName(), twitter);
  }

  /**
   * find-by method for unique field uniqueTag
   * @param uniqueTag the unique attribute
   * @return the unique DContact for the specified uniqueTag
   */
  public DContact findByUniqueTag(Object parent, java.lang.String uniqueTag) {
    return queryUniqueByField(parent, DContactMapper.Field.UNIQUETAG.getFieldName(), uniqueTag);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of DContacts for the specified updatedBy
   */
  public Iterable<DContact> queryByUpdatedBy(Object parent, java.lang.String updatedBy) {
    return queryByField(parent, DContactMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of DContacts for the specified updatedDate
   */
  public Iterable<DContact> queryByUpdatedDate(Object parent, java.util.Date updatedDate) {
    return queryByField(parent, DContactMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }

  /**
   * query-by method for field webPage
   * @param webPage the specified attribute
   * @return an Iterable of DContacts for the specified webPage
   */
  public Iterable<DContact> queryByWebPage(Object parent, java.lang.String webPage) {
    return queryByField(parent, DContactMapper.Field.WEBPAGE.getFieldName(), webPage);
  }

  /**
   * query-by method for field workPhone
   * @param workPhone the specified attribute
   * @return an Iterable of DContacts for the specified workPhone
   */
  public Iterable<DContact> queryByWorkPhone(Object parent, java.lang.String workPhone) {
    return queryByField(parent, DContactMapper.Field.WORKPHONE.getFieldName(), workPhone);
  }

  /**
   * query-by method for field zipCode
   * @param zipCode the specified attribute
   * @return an Iterable of DContacts for the specified zipCode
   */
  public Iterable<DContact> queryByZipCode(Object parent, java.lang.String zipCode) {
    return queryByField(parent, DContactMapper.Field.ZIPCODE.getFieldName(), zipCode);
  }


// ----------------------- DContact builder -------------------------------

  public static DContactMapper.Builder newBuilder() {
    return DContactMapper.newBuilder();
  }

}
