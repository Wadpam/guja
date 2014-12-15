package com.wadpam.guja.oauth2.dao;

/*
 * #%L
 * guja-core
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Collection;
import java.util.Date;

import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;
import com.wadpam.guja.oauth2.domain.DOAuth2User;

/**
 * The DOAuth2User domain-object specific mapping methods go here.
 *
 * Generated on 2014-12-14T21:20:34.275+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DOAuth2UserMapper
  implements Mapper<DOAuth2User, Long> {

  private final Supplier supplier;

  public enum Field {
    ID("id"),
    CREATEDBY("createdBy"),
    CREATEDDATE("createdDate"),
    DISPLAYNAME("displayName"),
    EMAIL("email"),
    PROFILELINK("profileLink"),
    ROLES("roles"),
    STATE("state"),
    THUMBNAILURL("thumbnailUrl"),
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

  public DOAuth2UserMapper(Supplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public Long fromKey(Object key) {
    return supplier.toLongKey(key);
  }

  @Override
  public DOAuth2User fromReadValue(Object value) {
    final DOAuth2User entity = new DOAuth2User();

    // set primary key:
    final Object key = supplier.getKey(value, Field.ID.getFieldName());
    entity.setId(supplier.toLongKey(key));

    // set all fields:
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setDisplayName(supplier.getString(value, Field.DISPLAYNAME.getFieldName()));
    entity.setEmail(supplier.getString(value, Field.EMAIL.getFieldName()));
    entity.setProfileLink(supplier.getString(value, Field.PROFILELINK.getFieldName()));
    entity.setRoles(supplier.getCollection(value, Field.ROLES.getFieldName()));
    entity.setState(supplier.getInteger(value, Field.STATE.getFieldName()));
    entity.setThumbnailUrl(supplier.getString(value, Field.THUMBNAILURL.getFieldName()));
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
  public Long getId(DOAuth2User entity) {
    return entity != null ? entity.getId() : null;
  }

  @Override
  public Object getParentKey(DOAuth2User entity) {
    return null;
  }

  @Override
  public void setParentKey(DOAuth2User entity, Object parentKey) {
    // this entity has no parent
  }

  @Override
  public void updateEntityPostWrite(DOAuth2User entity, Object key, Object value) {
    entity.setId(supplier.toLongKey(key));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
}

@Override
  public String getKind() {
    return DOAuth2User.class.getSimpleName();
  }

  @Override
  public Object toKey(Object parentKey, Long id) {
    return supplier.toKey(parentKey, getKind(), id);
  }

  @Override
  public Object toWriteValue(DOAuth2User entity) {
    final Long id = getId(entity);
    final Object parentKey = getParentKey(entity);
    final Object value = supplier.createWriteValue(parentKey, getKind(), id);

    // set all fields:
    supplier.setString(value, Field.CREATEDBY.getFieldName(), entity.getCreatedBy());
    supplier.setDate(value, Field.CREATEDDATE.getFieldName(), entity.getCreatedDate());
    supplier.setString(value, Field.DISPLAYNAME.getFieldName(), entity.getDisplayName());
    supplier.setString(value, Field.EMAIL.getFieldName(), entity.getEmail());
    supplier.setString(value, Field.PROFILELINK.getFieldName(), entity.getProfileLink());
    supplier.setCollection(value, Field.ROLES.getFieldName(), entity.getRoles());
    supplier.setInteger(value, Field.STATE.getFieldName(), entity.getState());
    supplier.setString(value, Field.THUMBNAILURL.getFieldName(), entity.getThumbnailUrl());
    supplier.setString(value, Field.UPDATEDBY.getFieldName(), entity.getUpdatedBy());
    supplier.setDate(value, Field.UPDATEDDATE.getFieldName(), entity.getUpdatedDate());
    return value;
  }

  public static DOAuth2UserBuilder newBuilder() {
    return new DOAuth2UserBuilder();
  }

  public static class DOAuth2UserBuilder extends AbstractEntityBuilder<DOAuth2User> {

    @Override
    protected DOAuth2User newInstance() {
      return new DOAuth2User();
    }

    public DOAuth2UserBuilder id(Long id) {
      entity.setId(id);
      return this;
    }

    public DOAuth2UserBuilder createdBy(String createdBy) {
      entity.setCreatedBy(createdBy);
      return this;
    }
    public DOAuth2UserBuilder createdDate(Date createdDate) {
      entity.setCreatedDate(createdDate);
      return this;
    }
    public DOAuth2UserBuilder displayName(String displayName) {
      entity.setDisplayName(displayName);
      return this;
    }
    public DOAuth2UserBuilder email(String email) {
      entity.setEmail(email);
      return this;
    }
    public DOAuth2UserBuilder profileLink(String profileLink) {
      entity.setProfileLink(profileLink);
      return this;
    }
    public DOAuth2UserBuilder roles(Collection roles) {
      entity.setRoles(roles);
      return this;
    }
    public DOAuth2UserBuilder state(Integer state) {
      entity.setState(state);
      return this;
    }
    public DOAuth2UserBuilder thumbnailUrl(String thumbnailUrl) {
      entity.setThumbnailUrl(thumbnailUrl);
      return this;
    }
    public DOAuth2UserBuilder updatedBy(String updatedBy) {
      entity.setUpdatedBy(updatedBy);
      return this;
    }
    public DOAuth2UserBuilder updatedDate(Date updatedDate) {
      entity.setUpdatedDate(updatedDate);
      return this;
    }
  }
}
