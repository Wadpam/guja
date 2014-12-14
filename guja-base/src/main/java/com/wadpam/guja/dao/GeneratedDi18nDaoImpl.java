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
import com.wadpam.guja.domain.Di18n;

/**
 * The Di18n domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2014-12-11T13:31:56.431+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDi18nDaoImpl
  extends AbstractDao<Di18n, java.lang.Long> {

  public GeneratedDi18nDaoImpl(Supplier supplier) {
    super(new Di18nMapper(supplier), supplier);
  }

// ----------------------- field finders -------------------------------
  /**
   * query-by method for field baseBundle
   * @param baseBundle the specified attribute
   * @return an Iterable of Di18ns for the specified baseBundle
   */
  public Iterable<Di18n> queryByBaseBundle(java.lang.String baseBundle) {
    return queryByField(null, Di18nMapper.Field.BASEBUNDLE.getFieldName(), baseBundle);
  }

  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of Di18ns for the specified createdBy
   */
  public Iterable<Di18n> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, Di18nMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of Di18ns for the specified createdDate
   */
  public Iterable<Di18n> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, Di18nMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field key
   * @param key the specified attribute
   * @return an Iterable of Di18ns for the specified key
   */
  public Iterable<Di18n> queryByKey(java.lang.String key) {
    return queryByField(null, Di18nMapper.Field.KEY.getFieldName(), key);
  }

  /**
   * query-by method for field locale
   * @param locale the specified attribute
   * @return an Iterable of Di18ns for the specified locale
   */
  public Iterable<Di18n> queryByLocale(java.lang.String locale) {
    return queryByField(null, Di18nMapper.Field.LOCALE.getFieldName(), locale);
  }

  /**
   * query-by method for field localizedMessage
   * @param localizedMessage the specified attribute
   * @return an Iterable of Di18ns for the specified localizedMessage
   */
  public Iterable<Di18n> queryByLocalizedMessage(java.lang.String localizedMessage) {
    return queryByField(null, Di18nMapper.Field.LOCALIZEDMESSAGE.getFieldName(), localizedMessage);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of Di18ns for the specified updatedBy
   */
  public Iterable<Di18n> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, Di18nMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of Di18ns for the specified updatedDate
   */
  public Iterable<Di18n> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, Di18nMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }


// ----------------------- query methods -------------------------------


}
