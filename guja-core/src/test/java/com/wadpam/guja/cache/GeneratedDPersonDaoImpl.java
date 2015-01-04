package com.wadpam.guja.cache;

import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Supplier;

/**
 * The DPerson domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2015-01-04T19:09:51.069+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDPersonDaoImpl
  extends AbstractDao<DPerson, java.lang.Long> {

  public GeneratedDPersonDaoImpl(Supplier supplier) {
    super(new DPersonMapper(supplier), supplier);
  }

// ----------------------- field finders -------------------------------
  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of DPersons for the specified createdBy
   */
  public Iterable<DPerson> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DPersonMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of DPersons for the specified createdDate
   */
  public Iterable<DPerson> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DPersonMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field firstName
   * @param firstName the specified attribute
   * @return an Iterable of DPersons for the specified firstName
   */
  public Iterable<DPerson> queryByFirstName(java.lang.String firstName) {
    return queryByField(null, DPersonMapper.Field.FIRSTNAME.getFieldName(), firstName);
  }

  /**
   * query-by method for field lastName
   * @param lastName the specified attribute
   * @return an Iterable of DPersons for the specified lastName
   */
  public Iterable<DPerson> queryByLastName(java.lang.String lastName) {
    return queryByField(null, DPersonMapper.Field.LASTNAME.getFieldName(), lastName);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of DPersons for the specified updatedBy
   */
  public Iterable<DPerson> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DPersonMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of DPersons for the specified updatedDate
   */
  public Iterable<DPerson> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DPersonMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }


// ----------------------- DPerson builder -------------------------------

  public static DPersonMapper.Builder newBuilder() {
    return DPersonMapper.newBuilder();
  }

}
