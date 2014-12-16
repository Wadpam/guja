package com.wadpam.guja.oauth2.dao;

import java.util.Collection;
import java.util.Date;

import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;
import com.wadpam.guja.oauth2.domain.DUser;

/**
 * The DUser domain-object specific mapping methods go here.
 *
 * Generated on 2014-12-16T11:27:40.160+0100.
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

  public static DUserBuilder newBuilder() {
    return new DUserBuilder();
  }

  public static class DUserBuilder extends AbstractEntityBuilder<DUser> {

    @Override
    protected DUser newInstance() {
      return new DUser();
    }

    public DUserBuilder id(Long id) {
      entity.setId(id);
      return this;
    }

    public DUserBuilder createdBy(String createdBy) {
      entity.setCreatedBy(createdBy);
      return this;
    }
    public DUserBuilder createdDate(Date createdDate) {
      entity.setCreatedDate(createdDate);
      return this;
    }
    public DUserBuilder displayName(String displayName) {
      entity.setDisplayName(displayName);
      return this;
    }
    public DUserBuilder email(String email) {
      entity.setEmail(email);
      return this;
    }
    public DUserBuilder friends(Collection friends) {
      entity.setFriends(friends);
      return this;
    }
    public DUserBuilder password(String password) {
      entity.setPassword(password);
      return this;
    }
    public DUserBuilder profileLink(String profileLink) {
      entity.setProfileLink(profileLink);
      return this;
    }
    public DUserBuilder roles(Collection roles) {
      entity.setRoles(roles);
      return this;
    }
    public DUserBuilder state(Integer state) {
      entity.setState(state);
      return this;
    }
    public DUserBuilder thumbnailUrl(String thumbnailUrl) {
      entity.setThumbnailUrl(thumbnailUrl);
      return this;
    }
    public DUserBuilder updatedBy(String updatedBy) {
      entity.setUpdatedBy(updatedBy);
      return this;
    }
    public DUserBuilder updatedDate(Date updatedDate) {
      entity.setUpdatedDate(updatedDate);
      return this;
    }
    public DUserBuilder username(String username) {
      entity.setUsername(username);
      return this;
    }
  }
}
