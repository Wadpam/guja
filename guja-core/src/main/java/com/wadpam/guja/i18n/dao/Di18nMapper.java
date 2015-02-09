package com.wadpam.guja.i18n.dao;

import java.util.Collection;
import java.util.Date;
import java.nio.ByteBuffer;

import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;
import com.wadpam.guja.i18n.domain.Di18n;

/**
 * The Di18n domain-object specific mapping methods go here.
 *
 * Generated on 2015-02-07T18:50:32.040+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class Di18nMapper
  implements Mapper<Di18n, Long> {

  private final Supplier supplier;

  public enum Field {
    ID("id"),
    BASEBUNDLE("baseBundle"),
    CREATEDBY("createdBy"),
    CREATEDDATE("createdDate"),
    KEY("key"),
    LOCALE("locale"),
    LOCALIZEDMESSAGE("localizedMessage"),
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

  public Di18nMapper(Supplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public Long fromKey(Object key) {
    return supplier.toLongKey(key);
  }

  @Override
  public Di18n fromReadValue(Object value) {
    final Di18n entity = new Di18n();

    // set primary key:
    final Object key = supplier.getKey(value, Field.ID.getFieldName());
    entity.setId(supplier.toLongKey(key));

    // set all fields:
    entity.setBaseBundle(supplier.getString(value, Field.BASEBUNDLE.getFieldName()));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setKey(supplier.getString(value, Field.KEY.getFieldName()));
    entity.setLocale(supplier.getString(value, Field.LOCALE.getFieldName()));
    entity.setLocalizedMessage(supplier.getString(value, Field.LOCALIZEDMESSAGE.getFieldName()));
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
  public Long getId(Di18n entity) {
    return entity != null ? entity.getId() : null;
  }

  @Override
  public Object getParentKey(Di18n entity) {
    return null;
  }

  @Override
  public void setParentKey(Di18n entity, Object parentKey) {
    // this entity has no parent
  }

  @Override
  public void updateEntityPostWrite(Di18n entity, Object key, Object value) {
    entity.setId(supplier.toLongKey(key));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
}

@Override
  public String getKind() {
    return Di18n.class.getSimpleName();
  }

  @Override
  public Object toKey(Object parentKey, Long id) {
    return supplier.toKey(parentKey, getKind(), id);
  }

  @Override
  public Object toWriteValue(Di18n entity) {
    final Long id = getId(entity);
    final Object parentKey = getParentKey(entity);
    final Object value = supplier.createWriteValue(parentKey, getKind(), id);

    // set all fields:
    supplier.setString(value, Field.BASEBUNDLE.getFieldName(), entity.getBaseBundle());
    supplier.setString(value, Field.CREATEDBY.getFieldName(), entity.getCreatedBy());
    supplier.setDate(value, Field.CREATEDDATE.getFieldName(), entity.getCreatedDate());
    supplier.setString(value, Field.KEY.getFieldName(), entity.getKey());
    supplier.setString(value, Field.LOCALE.getFieldName(), entity.getLocale());
    supplier.setString(value, Field.LOCALIZEDMESSAGE.getFieldName(), entity.getLocalizedMessage());
    supplier.setString(value, Field.UPDATEDBY.getFieldName(), entity.getUpdatedBy());
    supplier.setDate(value, Field.UPDATEDDATE.getFieldName(), entity.getUpdatedDate());
    return value;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends AbstractEntityBuilder<Di18n> {

    protected Builder() {
      super(new Di18n());
    }

    public Builder id(Long id) {
      entity.setId(id);
      return this;
    }

    public Builder baseBundle(String baseBundle) {
      entity.setBaseBundle(baseBundle);
      return this;
    }

    public Builder createdBy(String createdBy) {
      entity.setCreatedBy(createdBy);
      return this;
    }

    public Builder createdDate(Date createdDate) {
      entity.setCreatedDate(createdDate);
      return this;
    }

    public Builder key(String key) {
      entity.setKey(key);
      return this;
    }

    public Builder locale(String locale) {
      entity.setLocale(locale);
      return this;
    }

    public Builder localizedMessage(String localizedMessage) {
      entity.setLocalizedMessage(localizedMessage);
      return this;
    }

    public Builder updatedBy(String updatedBy) {
      entity.setUpdatedBy(updatedBy);
      return this;
    }

    public Builder updatedDate(Date updatedDate) {
      entity.setUpdatedDate(updatedDate);
      return this;
    }

  }
}
