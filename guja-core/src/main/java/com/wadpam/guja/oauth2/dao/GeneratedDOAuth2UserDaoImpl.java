package com.wadpam.guja.oauth2.dao;

import com.wadpam.guja.oauth2.domain.DOAuth2User;
import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Supplier;

/**
 * The DOAuth2User domain-object specific finders and methods go in this POJO.
 * <p/>
 * Generated on 2014-12-11T13:31:52.156+0100.
 *
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
   *
   * @param createdBy the specified attribute
   * @return an Iterable of DOAuth2Users for the specified createdBy
   */
  public Iterable<DOAuth2User> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DOAuth2UserMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   *
   * @param createdDate the specified attribute
   * @return an Iterable of DOAuth2Users for the specified createdDate
   */
  public Iterable<DOAuth2User> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DOAuth2UserMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field displayName
   *
   * @param displayName the specified attribute
   * @return an Iterable of DOAuth2Users for the specified displayName
   */
  public Iterable<DOAuth2User> queryByDisplayName(java.lang.String displayName) {
    return queryByField(null, DOAuth2UserMapper.Field.DISPLAYNAME.getFieldName(), displayName);
  }

  /**
   * query-by method for field email
   *
   * @param email the specified attribute
   * @return an Iterable of DOAuth2Users for the specified email
   */
  public Iterable<DOAuth2User> queryByEmail(java.lang.String email) {
    return queryByField(null, DOAuth2UserMapper.Field.EMAIL.getFieldName(), email);
  }

  /**
   * query-by method for field profileLink
   *
   * @param profileLink the specified attribute
   * @return an Iterable of DOAuth2Users for the specified profileLink
   */
  public Iterable<DOAuth2User> queryByProfileLink(java.lang.String profileLink) {
    return queryByField(null, DOAuth2UserMapper.Field.PROFILELINK.getFieldName(), profileLink);
  }

  /**
   * query-by method for field roles
   *
   * @param roles the specified attribute
   * @return an Iterable of DOAuth2Users for the specified roles
   */
  public Iterable<DOAuth2User> queryByRoles(java.lang.Object roles) {
    return queryByField(null, DOAuth2UserMapper.Field.ROLES.getFieldName(), roles);
  }

  /**
   * query-by method for field state
   *
   * @param state the specified attribute
   * @return an Iterable of DOAuth2Users for the specified state
   */
  public Iterable<DOAuth2User> queryByState(java.lang.Integer state) {
    return queryByField(null, DOAuth2UserMapper.Field.STATE.getFieldName(), state);
  }

  /**
   * query-by method for field thumbnailUrl
   *
   * @param thumbnailUrl the specified attribute
   * @return an Iterable of DOAuth2Users for the specified thumbnailUrl
   */
  public Iterable<DOAuth2User> queryByThumbnailUrl(java.lang.String thumbnailUrl) {
    return queryByField(null, DOAuth2UserMapper.Field.THUMBNAILURL.getFieldName(), thumbnailUrl);
  }

  /**
   * query-by method for field updatedBy
   *
   * @param updatedBy the specified attribute
   * @return an Iterable of DOAuth2Users for the specified updatedBy
   */
  public Iterable<DOAuth2User> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DOAuth2UserMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   *
   * @param updatedDate the specified attribute
   * @return an Iterable of DOAuth2Users for the specified updatedDate
   */
  public Iterable<DOAuth2User> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DOAuth2UserMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }


// ----------------------- query methods -------------------------------


}
