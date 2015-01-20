package com.wadpam.guja.oauth2.dao;

import java.util.Collection;
import java.util.Date;
import java.nio.ByteBuffer;

import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;
import com.wadpam.guja.oauth2.domain.DUser;

/**
 * The DUser domain-object specific mapping methods go here.
 *
 * Generated on 2015-01-20T21:35:58.613+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DUserMapper
  implements Mapper<DUser, Long> {

  private final Supplier supplier;

  public enum Field {
    ID("id"),
    CREATEDBY("createdBy"),
    CREATEDDATE("createdDate"),
    DISPLAYNAME("displayName"),
    EMAIL("email"),
    FRIENDS("friends"),
    PASSWORD("password"),
    PROFILELINK("profileLink"),
    ROLES("roles"),
    STATE("state"),
    THUMBNAILURL("thumbnailUrl"),
    UPDATEDBY("updatedBy"),
    UPDATEDDATE("updatedDate"),
    USERNAME("username");

    private final String fieldName;

    Field(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }

  public DUserMapper(Supplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public Long fromKey(Object key) {
    return supplier.toLongKey(key);
  }

  @Override
  public DUser fromReadValue(Object value) {
    final DUser entity = new DUser();

    // set primary key:
    final Object key = supplier.getKey(value, Field.ID.getFieldName());
    entity.setId(supplier.toLongKey(key));

    // set all fields:
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setDisplayName(supplier.getString(value, Field.DISPLAYNAME.getFieldName()));
    entity.setEmail(supplier.getString(value, Field.EMAIL.getFieldName()));
    entity.setFriends(supplier.getCollection(value, Field.FRIENDS.getFieldName()));
    entity.setPassword(supplier.getString(value, Field.PASSWORD.getFieldName()));
    entity.setProfileLink(supplier.getString(value, Field.PROFILELINK.getFieldName()));
    entity.setRoles(supplier.getCollection(value, Field.ROLES.getFieldName()));
    entity.setState(supplier.getInteger(value, Field.STATE.getFieldName()));
    entity.setThumbnailUrl(supplier.getString(value, Field.THUMBNAILURL.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
    entity.setUsername(supplier.getString(value, Field.USERNAME.getFieldName()));
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
  public Long getId(DUser entity) {
    return entity != null ? entity.getId() : null;
  }

  @Override
  public Object getParentKey(DUser entity) {
    return null;
  }

  @Override
  public void setParentKey(DUser entity, Object parentKey) {
    // this entity has no parent
  }

  @Override
  public void updateEntityPostWrite(DUser entity, Object key, Object value) {
    entity.setId(supplier.toLongKey(key));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
}

@Override
  public String getKind() {
    return DUser.class.getSimpleName();
  }

  @Override
  public Object toKey(Object parentKey, Long id) {
    return supplier.toKey(parentKey, getKind(), id);
  }

  @Override
  public Object toWriteValue(DUser entity) {
    final Long id = getId(entity);
    final Object parentKey = getParentKey(entity);
    final Object value = supplier.createWriteValue(parentKey, getKind(), id);

    // set all fields:
    supplier.setString(value, Field.CREATEDBY.getFieldName(), entity.getCreatedBy());
    supplier.setDate(value, Field.CREATEDDATE.getFieldName(), entity.getCreatedDate());
    supplier.setString(value, Field.DISPLAYNAME.getFieldName(), entity.getDisplayName());
    supplier.setString(value, Field.EMAIL.getFieldName(), entity.getEmail());
    supplier.setCollection(value, Field.FRIENDS.getFieldName(), entity.getFriends());
    supplier.setString(value, Field.PASSWORD.getFieldName(), entity.getPassword());
    supplier.setString(value, Field.PROFILELINK.getFieldName(), entity.getProfileLink());
    supplier.setCollection(value, Field.ROLES.getFieldName(), entity.getRoles());
    supplier.setInteger(value, Field.STATE.getFieldName(), entity.getState());
    supplier.setString(value, Field.THUMBNAILURL.getFieldName(), entity.getThumbnailUrl());
    supplier.setString(value, Field.UPDATEDBY.getFieldName(), entity.getUpdatedBy());
    supplier.setDate(value, Field.UPDATEDDATE.getFieldName(), entity.getUpdatedDate());
    supplier.setString(value, Field.USERNAME.getFieldName(), entity.getUsername());
    return value;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends AbstractEntityBuilder<DUser> {

    protected Builder() {
      super(new DUser());
    }

    public Builder id(Long id) {
      entity.setId(id);
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

    public Builder displayName(String displayName) {
      entity.setDisplayName(displayName);
      return this;
    }

    public Builder email(String email) {
      entity.setEmail(email);
      return this;
    }

    public Builder friends(Collection friends) {
      entity.setFriends(friends);
      return this;
    }

    public Builder password(String password) {
      entity.setPassword(password);
      return this;
    }

    public Builder profileLink(String profileLink) {
      entity.setProfileLink(profileLink);
      return this;
    }

    public Builder roles(Collection roles) {
      entity.setRoles(roles);
      return this;
    }

    public Builder state(Integer state) {
      entity.setState(state);
      return this;
    }

    public Builder thumbnailUrl(String thumbnailUrl) {
      entity.setThumbnailUrl(thumbnailUrl);
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

    public Builder username(String username) {
      entity.setUsername(username);
      return this;
    }

  }
}
