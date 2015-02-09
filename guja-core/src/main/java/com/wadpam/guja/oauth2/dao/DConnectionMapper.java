package com.wadpam.guja.oauth2.dao;

import java.util.Collection;
import java.util.Date;
import java.nio.ByteBuffer;

import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;
import com.wadpam.guja.oauth2.domain.DConnection;

/**
 * The DConnection domain-object specific mapping methods go here.
 *
 * Generated on 2015-02-07T18:50:32.440+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DConnectionMapper
  implements Mapper<DConnection, Long> {

  private final Supplier supplier;

  public enum Field {
    ID("id"),
    ACCESSTOKEN("accessToken"),
    APPARG0("appArg0"),
    CREATEDBY("createdBy"),
    CREATEDDATE("createdDate"),
    DISPLAYNAME("displayName"),
    EXPIRETIME("expireTime"),
    IMAGEURL("imageUrl"),
    PROFILEURL("profileUrl"),
    PROVIDERID("providerId"),
    PROVIDERUSERID("providerUserId"),
    REFRESHTOKEN("refreshToken"),
    SECRET("secret"),
    UPDATEDBY("updatedBy"),
    UPDATEDDATE("updatedDate"),
    USERID("userId"),
    USERROLES("userRoles");

    private final String fieldName;

    Field(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }

  public DConnectionMapper(Supplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public Long fromKey(Object key) {
    return supplier.toLongKey(key);
  }

  @Override
  public DConnection fromReadValue(Object value) {
    final DConnection entity = new DConnection();

    // set primary key:
    final Object key = supplier.getKey(value, Field.ID.getFieldName());
    entity.setId(supplier.toLongKey(key));

    // set all fields:
    entity.setAccessToken(supplier.getString(value, Field.ACCESSTOKEN.getFieldName()));
    entity.setAppArg0(supplier.getString(value, Field.APPARG0.getFieldName()));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setDisplayName(supplier.getString(value, Field.DISPLAYNAME.getFieldName()));
    entity.setExpireTime(supplier.getDate(value, Field.EXPIRETIME.getFieldName()));
    entity.setImageUrl(supplier.getString(value, Field.IMAGEURL.getFieldName()));
    entity.setProfileUrl(supplier.getString(value, Field.PROFILEURL.getFieldName()));
    entity.setProviderId(supplier.getString(value, Field.PROVIDERID.getFieldName()));
    entity.setProviderUserId(supplier.getString(value, Field.PROVIDERUSERID.getFieldName()));
    entity.setRefreshToken(supplier.getString(value, Field.REFRESHTOKEN.getFieldName()));
    entity.setSecret(supplier.getString(value, Field.SECRET.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
    entity.setUserId(supplier.getLong(value, Field.USERID.getFieldName()));
    entity.setUserRoles(supplier.getString(value, Field.USERROLES.getFieldName()));
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
  public Long getId(DConnection entity) {
    return entity != null ? entity.getId() : null;
  }

  @Override
  public Object getParentKey(DConnection entity) {
    return null;
  }

  @Override
  public void setParentKey(DConnection entity, Object parentKey) {
    // this entity has no parent
  }

  @Override
  public void updateEntityPostWrite(DConnection entity, Object key, Object value) {
    entity.setId(supplier.toLongKey(key));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
}

@Override
  public String getKind() {
    return DConnection.class.getSimpleName();
  }

  @Override
  public Object toKey(Object parentKey, Long id) {
    return supplier.toKey(parentKey, getKind(), id);
  }

  @Override
  public Object toWriteValue(DConnection entity) {
    final Long id = getId(entity);
    final Object parentKey = getParentKey(entity);
    final Object value = supplier.createWriteValue(parentKey, getKind(), id);

    // set all fields:
    supplier.setString(value, Field.ACCESSTOKEN.getFieldName(), entity.getAccessToken());
    supplier.setString(value, Field.APPARG0.getFieldName(), entity.getAppArg0());
    supplier.setString(value, Field.CREATEDBY.getFieldName(), entity.getCreatedBy());
    supplier.setDate(value, Field.CREATEDDATE.getFieldName(), entity.getCreatedDate());
    supplier.setString(value, Field.DISPLAYNAME.getFieldName(), entity.getDisplayName());
    supplier.setDate(value, Field.EXPIRETIME.getFieldName(), entity.getExpireTime());
    supplier.setString(value, Field.IMAGEURL.getFieldName(), entity.getImageUrl());
    supplier.setString(value, Field.PROFILEURL.getFieldName(), entity.getProfileUrl());
    supplier.setString(value, Field.PROVIDERID.getFieldName(), entity.getProviderId());
    supplier.setString(value, Field.PROVIDERUSERID.getFieldName(), entity.getProviderUserId());
    supplier.setString(value, Field.REFRESHTOKEN.getFieldName(), entity.getRefreshToken());
    supplier.setString(value, Field.SECRET.getFieldName(), entity.getSecret());
    supplier.setString(value, Field.UPDATEDBY.getFieldName(), entity.getUpdatedBy());
    supplier.setDate(value, Field.UPDATEDDATE.getFieldName(), entity.getUpdatedDate());
    supplier.setLong(value, Field.USERID.getFieldName(), entity.getUserId());
    supplier.setString(value, Field.USERROLES.getFieldName(), entity.getUserRoles());
    return value;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends AbstractEntityBuilder<DConnection> {

    protected Builder() {
      super(new DConnection());
    }

    public Builder id(Long id) {
      entity.setId(id);
      return this;
    }

    public Builder accessToken(String accessToken) {
      entity.setAccessToken(accessToken);
      return this;
    }

    public Builder appArg0(String appArg0) {
      entity.setAppArg0(appArg0);
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

    public Builder expireTime(Date expireTime) {
      entity.setExpireTime(expireTime);
      return this;
    }

    public Builder imageUrl(String imageUrl) {
      entity.setImageUrl(imageUrl);
      return this;
    }

    public Builder profileUrl(String profileUrl) {
      entity.setProfileUrl(profileUrl);
      return this;
    }

    public Builder providerId(String providerId) {
      entity.setProviderId(providerId);
      return this;
    }

    public Builder providerUserId(String providerUserId) {
      entity.setProviderUserId(providerUserId);
      return this;
    }

    public Builder refreshToken(String refreshToken) {
      entity.setRefreshToken(refreshToken);
      return this;
    }

    public Builder secret(String secret) {
      entity.setSecret(secret);
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

    public Builder userId(Long userId) {
      entity.setUserId(userId);
      return this;
    }

    public Builder userRoles(String userRoles) {
      entity.setUserRoles(userRoles);
      return this;
    }

  }
}
