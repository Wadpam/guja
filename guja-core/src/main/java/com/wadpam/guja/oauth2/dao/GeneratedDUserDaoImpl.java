package com.wadpam.guja.oauth2.dao;

import com.wadpam.guja.oauth2.domain.DUser;
import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Supplier;

/**
 * The DUser domain-object specific finders and methods go in this POJO.
 * <p/>
 * Generated on 2014-12-11T13:31:52.156+0100.
 *
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
   *
   * @param createdBy the specified attribute
   * @return an Iterable of DUsers for the specified createdBy
   */
  public Iterable<DUser> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DUserMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   *
   * @param createdDate the specified attribute
   * @return an Iterable of DUsers for the specified createdDate
   */
  public Iterable<DUser> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DUserMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field displayName
   *
   * @param displayName the specified attribute
   * @return an Iterable of DUsers for the specified displayName
   */
  public Iterable<DUser> queryByDisplayName(java.lang.String displayName) {
    return queryByField(null, DUserMapper.Field.DISPLAYNAME.getFieldName(), displayName);
  }

  /**
   * query-by method for field email
   *
   * @param email the specified attribute
   * @return an Iterable of DUsers for the specified email
   */
  public Iterable<DUser> queryByEmail(java.lang.String email) {
    return queryByField(null, DUserMapper.Field.EMAIL.getFieldName(), email);
  }

  /**
   * query-by method for field friends
   *
   * @param friends the specified attribute
   * @return an Iterable of DUsers for the specified friends
   */
  public Iterable<DUser> queryByFriends(java.lang.Object friends) {
    return queryByField(null, DUserMapper.Field.FRIENDS.getFieldName(), friends);
  }

  /**
   * query-by method for field password
   *
   * @param password the specified attribute
   * @return an Iterable of DUsers for the specified password
   */
  public Iterable<DUser> queryByPassword(java.lang.String password) {
    return queryByField(null, DUserMapper.Field.PASSWORD.getFieldName(), password);
  }

  /**
   * query-by method for field profileLink
   *
   * @param profileLink the specified attribute
   * @return an Iterable of DUsers for the specified profileLink
   */
  public Iterable<DUser> queryByProfileLink(java.lang.String profileLink) {
    return queryByField(null, DUserMapper.Field.PROFILELINK.getFieldName(), profileLink);
  }

  /**
   * query-by method for field roles
   *
   * @param roles the specified attribute
   * @return an Iterable of DUsers for the specified roles
   */
  public Iterable<DUser> queryByRoles(java.lang.Object roles) {
    return queryByField(null, DUserMapper.Field.ROLES.getFieldName(), roles);
  }

  /**
   * query-by method for field state
   *
   * @param state the specified attribute
   * @return an Iterable of DUsers for the specified state
   */
  public Iterable<DUser> queryByState(java.lang.Integer state) {
    return queryByField(null, DUserMapper.Field.STATE.getFieldName(), state);
  }

  /**
   * query-by method for field thumbnailUrl
   *
   * @param thumbnailUrl the specified attribute
   * @return an Iterable of DUsers for the specified thumbnailUrl
   */
  public Iterable<DUser> queryByThumbnailUrl(java.lang.String thumbnailUrl) {
    return queryByField(null, DUserMapper.Field.THUMBNAILURL.getFieldName(), thumbnailUrl);
  }

  /**
   * query-by method for field updatedBy
   *
   * @param updatedBy the specified attribute
   * @return an Iterable of DUsers for the specified updatedBy
   */
  public Iterable<DUser> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DUserMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   *
   * @param updatedDate the specified attribute
   * @return an Iterable of DUsers for the specified updatedDate
   */
  public Iterable<DUser> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DUserMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }

  /**
   * find-by method for unique field username
   *
   * @param username the unique attribute
   * @return the unique DUser for the specified username
   */
  public DUser findByUsername(java.lang.String username) {
    return queryUniqueByField(null, DUserMapper.Field.USERNAME.getFieldName(), username);
  }


// ----------------------- query methods -------------------------------


}
