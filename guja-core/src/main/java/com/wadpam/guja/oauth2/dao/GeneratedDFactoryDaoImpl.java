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
import com.wadpam.guja.oauth2.domain.DFactory;
import net.sf.mardao.core.Cached;
import net.sf.mardao.core.CacheConfig;
import java.io.IOException;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheValue;


/**
 * The DFactory domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2015-02-07T18:50:32.440+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDFactoryDaoImpl
  extends AbstractDao<DFactory, java.lang.String> {

  public GeneratedDFactoryDaoImpl(Supplier supplier) {
    super(new DFactoryMapper(supplier), supplier);
  }

// ----------------------- Caching -------------------------------------

  // Cache crud methods
  @CacheResult
  @Override
  public DFactory get(@CacheKey Object parentKey, @CacheKey java.lang.String id) throws IOException {
    return super.get(parentKey, id);
  }

  @CachePut
  @Override
  public java.lang.String put(@CacheKey Object parentKey, @CacheKey java.lang.String id, @CacheValue DFactory entity) throws IOException {
    return super.put(parentKey, id, entity);
  }

  @CacheRemove
  @Override
  public void delete(@CacheKey Object parentKey, @CacheKey java.lang.String id) throws IOException {
    super.delete(parentKey, id);
  }


// ----------------------- field finders -------------------------------
  /**
   * query-by method for field baseUrl
   * @param baseUrl the specified attribute
   * @return an Iterable of DFactorys for the specified baseUrl
   */
  public Iterable<DFactory> queryByBaseUrl(java.lang.String baseUrl) {
    return queryByField(null, DFactoryMapper.Field.BASEURL.getFieldName(), baseUrl);
  }

  /**
   * query-by method for field clientId
   * @param clientId the specified attribute
   * @return an Iterable of DFactorys for the specified clientId
   */
  public Iterable<DFactory> queryByClientId(java.lang.String clientId) {
    return queryByField(null, DFactoryMapper.Field.CLIENTID.getFieldName(), clientId);
  }

  /**
   * query-by method for field clientSecret
   * @param clientSecret the specified attribute
   * @return an Iterable of DFactorys for the specified clientSecret
   */
  public Iterable<DFactory> queryByClientSecret(java.lang.String clientSecret) {
    return queryByField(null, DFactoryMapper.Field.CLIENTSECRET.getFieldName(), clientSecret);
  }

  /**
   * query-by method for field createdBy
   * @param createdBy the specified attribute
   * @return an Iterable of DFactorys for the specified createdBy
   */
  public Iterable<DFactory> queryByCreatedBy(java.lang.String createdBy) {
    return queryByField(null, DFactoryMapper.Field.CREATEDBY.getFieldName(), createdBy);
  }

  /**
   * query-by method for field createdDate
   * @param createdDate the specified attribute
   * @return an Iterable of DFactorys for the specified createdDate
   */
  public Iterable<DFactory> queryByCreatedDate(java.util.Date createdDate) {
    return queryByField(null, DFactoryMapper.Field.CREATEDDATE.getFieldName(), createdDate);
  }

  /**
   * query-by method for field updatedBy
   * @param updatedBy the specified attribute
   * @return an Iterable of DFactorys for the specified updatedBy
   */
  public Iterable<DFactory> queryByUpdatedBy(java.lang.String updatedBy) {
    return queryByField(null, DFactoryMapper.Field.UPDATEDBY.getFieldName(), updatedBy);
  }

  /**
   * query-by method for field updatedDate
   * @param updatedDate the specified attribute
   * @return an Iterable of DFactorys for the specified updatedDate
   */
  public Iterable<DFactory> queryByUpdatedDate(java.util.Date updatedDate) {
    return queryByField(null, DFactoryMapper.Field.UPDATEDDATE.getFieldName(), updatedDate);
  }


// ----------------------- DFactory builder -------------------------------

  public static DFactoryMapper.Builder newBuilder() {
    return DFactoryMapper.newBuilder();
  }

}
