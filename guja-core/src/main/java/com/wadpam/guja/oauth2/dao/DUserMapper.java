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
 * Generated on 2015-02-07T18:50:32.440+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DUserMapper
  implements Mapper<DUser, Long> {

  private final Supplier supplier;

  public enum Field {
    ID("id"),
    ADDRESS1("address1"),
    ADDRESS2("address2"),
    BIRTHINFO("birthInfo"),
    CITY("city"),
    COUNTRY("country"),
    CREATEDBY("createdBy"),
    CREATEDDATE("createdDate"),
    DISPLAYNAME("displayName"),
    EMAIL("email"),
    FIRSTNAME("firstName"),
    FRIENDS("friends"),
    LASTNAME("lastName"),
    PASSWORD("password"),
    PHONENUMBER1("phoneNumber1"),
    PHONENUMBER2("phoneNumber2"),
    PROFILELINK("profileLink"),
    ROLES("roles"),
    STATE("state"),
    THUMBNAILURL("thumbnailUrl"),
    UPDATEDBY("updatedBy"),
    UPDATEDDATE("updatedDate"),
    USERNAME("username"),
    ZIPCODE("zipCode");

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
    entity.setAddress1(supplier.getString(value, Field.ADDRESS1.getFieldName()));
    entity.setAddress2(supplier.getString(value, Field.ADDRESS2.getFieldName()));
    entity.setBirthInfo(supplier.getString(value, Field.BIRTHINFO.getFieldName()));
    entity.setCity(supplier.getString(value, Field.CITY.getFieldName()));
    entity.setCountry(supplier.getString(value, Field.COUNTRY.getFieldName()));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setDisplayName(supplier.getString(value, Field.DISPLAYNAME.getFieldName()));
    entity.setEmail(supplier.getString(value, Field.EMAIL.getFieldName()));
    entity.setFirstName(supplier.getString(value, Field.FIRSTNAME.getFieldName()));
    entity.setFriends(supplier.getCollection(value, Field.FRIENDS.getFieldName()));
    entity.setLastName(supplier.getString(value, Field.LASTNAME.getFieldName()));
    entity.setPassword(supplier.getString(value, Field.PASSWORD.getFieldName()));
    entity.setPhoneNumber1(supplier.getString(value, Field.PHONENUMBER1.getFieldName()));
    entity.setPhoneNumber2(supplier.getString(value, Field.PHONENUMBER2.getFieldName()));
    entity.setProfileLink(supplier.getString(value, Field.PROFILELINK.getFieldName()));
    entity.setRoles(supplier.getCollection(value, Field.ROLES.getFieldName()));
    entity.setState(supplier.getInteger(value, Field.STATE.getFieldName()));
    entity.setThumbnailUrl(supplier.getString(value, Field.THUMBNAILURL.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
    entity.setUsername(supplier.getString(value, Field.USERNAME.getFieldName()));
    entity.setZipCode(supplier.getString(value, Field.ZIPCODE.getFieldName()));
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
    supplier.setString(value, Field.ADDRESS1.getFieldName(), entity.getAddress1());
    supplier.setString(value, Field.ADDRESS2.getFieldName(), entity.getAddress2());
    supplier.setString(value, Field.BIRTHINFO.getFieldName(), entity.getBirthInfo());
    supplier.setString(value, Field.CITY.getFieldName(), entity.getCity());
    supplier.setString(value, Field.COUNTRY.getFieldName(), entity.getCountry());
    supplier.setString(value, Field.CREATEDBY.getFieldName(), entity.getCreatedBy());
    supplier.setDate(value, Field.CREATEDDATE.getFieldName(), entity.getCreatedDate());
    supplier.setString(value, Field.DISPLAYNAME.getFieldName(), entity.getDisplayName());
    supplier.setString(value, Field.EMAIL.getFieldName(), entity.getEmail());
    supplier.setString(value, Field.FIRSTNAME.getFieldName(), entity.getFirstName());
    supplier.setCollection(value, Field.FRIENDS.getFieldName(), entity.getFriends());
    supplier.setString(value, Field.LASTNAME.getFieldName(), entity.getLastName());
    supplier.setString(value, Field.PASSWORD.getFieldName(), entity.getPassword());
    supplier.setString(value, Field.PHONENUMBER1.getFieldName(), entity.getPhoneNumber1());
    supplier.setString(value, Field.PHONENUMBER2.getFieldName(), entity.getPhoneNumber2());
    supplier.setString(value, Field.PROFILELINK.getFieldName(), entity.getProfileLink());
    supplier.setCollection(value, Field.ROLES.getFieldName(), entity.getRoles());
    supplier.setInteger(value, Field.STATE.getFieldName(), entity.getState());
    supplier.setString(value, Field.THUMBNAILURL.getFieldName(), entity.getThumbnailUrl());
    supplier.setString(value, Field.UPDATEDBY.getFieldName(), entity.getUpdatedBy());
    supplier.setDate(value, Field.UPDATEDDATE.getFieldName(), entity.getUpdatedDate());
    supplier.setString(value, Field.USERNAME.getFieldName(), entity.getUsername());
    supplier.setString(value, Field.ZIPCODE.getFieldName(), entity.getZipCode());
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

    public Builder address1(String address1) {
      entity.setAddress1(address1);
      return this;
    }

    public Builder address2(String address2) {
      entity.setAddress2(address2);
      return this;
    }

    public Builder birthInfo(String birthInfo) {
      entity.setBirthInfo(birthInfo);
      return this;
    }

    public Builder city(String city) {
      entity.setCity(city);
      return this;
    }

    public Builder country(String country) {
      entity.setCountry(country);
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

    public Builder firstName(String firstName) {
      entity.setFirstName(firstName);
      return this;
    }

    public Builder friends(Collection friends) {
      entity.setFriends(friends);
      return this;
    }

    public Builder lastName(String lastName) {
      entity.setLastName(lastName);
      return this;
    }

    public Builder password(String password) {
      entity.setPassword(password);
      return this;
    }

    public Builder phoneNumber1(String phoneNumber1) {
      entity.setPhoneNumber1(phoneNumber1);
      return this;
    }

    public Builder phoneNumber2(String phoneNumber2) {
      entity.setPhoneNumber2(phoneNumber2);
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

    public Builder zipCode(String zipCode) {
      entity.setZipCode(zipCode);
      return this;
    }

  }
}
