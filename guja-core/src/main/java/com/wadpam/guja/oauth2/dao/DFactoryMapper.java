package com.wadpam.guja.oauth2.dao;

import com.wadpam.guja.oauth2.domain.DFactory;
import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;

import java.util.Date;

/**
 * The DFactory domain-object specific mapping methods go here.
 * <p/>
 * Generated on 2014-12-11T13:31:52.156+0100.
 *
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DFactoryMapper
    implements Mapper<DFactory, String> {

  private final Supplier supplier;

  public enum Field {
    ID("id"),
    BASEURL("baseUrl"),
    CLIENTID("clientId"),
    CLIENTSECRET("clientSecret"),
    CREATEDBY("createdBy"),
    CREATEDDATE("createdDate"),
    UPDATEDBY("updatedBy"),
    UPDATEDDATE("updatedDate");

    private final String fieldName;

    Field(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }

  public DFactoryMapper(Supplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public String fromKey(Object key) {
    return supplier.toStringKey(key);
  }

  @Override
  public DFactory fromReadValue(Object value) {
    final DFactory entity = new DFactory();

    // set primary key:
    final Object key = supplier.getKey(value, Field.ID.getFieldName());
    entity.setId(supplier.toStringKey(key));

    // set all fields:
    entity.setBaseUrl(supplier.getString(value, Field.BASEURL.getFieldName()));
    entity.setClientId(supplier.getString(value, Field.CLIENTID.getFieldName()));
    entity.setClientSecret(supplier.getString(value, Field.CLIENTSECRET.getFieldName()));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
    return entity;
  }

  @Override
  public String getCreatedByColumnName() {
    return Field.CREATEDBY.getFieldName();
  }

  @Override
  public String getCreatedDateColumnName() {
    return Field.CREATEDDATE.getFieldName();
  }

  @Override
  public String getUpdatedByColumnName() {
    return Field.UPDATEDBY.getFieldName();
  }

  @Override
  public String getUpdatedDateColumnName() {
    return Field.UPDATEDDATE.getFieldName();
  }

  @Override
  public String getId(DFactory entity) {
    return entity != null ? entity.getId() : null;
  }

  @Override
  public Object getParentKey(DFactory entity) {
    return null;
  }

  @Override
  public void setParentKey(DFactory entity, Object parentKey) {
    // this entity has no parent
  }

  @Override
  public void updateEntityPostWrite(DFactory entity, Object key, Object value) {
    entity.setId(supplier.toStringKey(key));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
  }

  @Override
  public String getKind() {
    return DFactory.class.getSimpleName();
  }

  @Override
  public Object toKey(Object parentKey, String id) {
    return supplier.toKey(parentKey, getKind(), id);
  }

  @Override
  public Object toWriteValue(DFactory entity) {
    final String id = getId(entity);
    final Object parentKey = getParentKey(entity);
    final Object value = supplier.createWriteValue(parentKey, getKind(), id);

    // set all fields:
    supplier.setString(value, Field.BASEURL.getFieldName(), entity.getBaseUrl());
    supplier.setString(value, Field.CLIENTID.getFieldName(), entity.getClientId());
    supplier.setString(value, Field.CLIENTSECRET.getFieldName(), entity.getClientSecret());
    supplier.setString(value, Field.CREATEDBY.getFieldName(), entity.getCreatedBy());
    supplier.setDate(value, Field.CREATEDDATE.getFieldName(), entity.getCreatedDate());
    supplier.setString(value, Field.UPDATEDBY.getFieldName(), entity.getUpdatedBy());
    supplier.setDate(value, Field.UPDATEDDATE.getFieldName(), entity.getUpdatedDate());
    return value;
  }

  public static DFactoryBuilder newBuilder() {
    return new DFactoryBuilder();
  }

  public static class DFactoryBuilder extends AbstractEntityBuilder<DFactory> {

    @Override
    protected DFactory newInstance() {
      return new DFactory();
    }

    public DFactoryBuilder id(String id) {
      entity.setId(id);
      return this;
    }

    public DFactoryBuilder baseUrl(String baseUrl) {
      entity.setBaseUrl(baseUrl);
      return this;
    }

    public DFactoryBuilder clientId(String clientId) {
      entity.setClientId(clientId);
      return this;
    }

    public DFactoryBuilder clientSecret(String clientSecret) {
      entity.setClientSecret(clientSecret);
      return this;
    }

    public DFactoryBuilder createdBy(String createdBy) {
      entity.setCreatedBy(createdBy);
      return this;
    }

    public DFactoryBuilder createdDate(Date createdDate) {
      entity.setCreatedDate(createdDate);
      return this;
    }

    public DFactoryBuilder updatedBy(String updatedBy) {
      entity.setUpdatedBy(updatedBy);
      return this;
    }

    public DFactoryBuilder updatedDate(Date updatedDate) {
      entity.setUpdatedDate(updatedDate);
      return this;
    }
  }
}
