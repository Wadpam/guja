package com.wadpam.guja.dao;

import java.util.Collection;
import java.util.Date;
import java.nio.ByteBuffer;

import net.sf.mardao.dao.Mapper;
import net.sf.mardao.dao.Supplier;
import net.sf.mardao.domain.AbstractEntityBuilder;
import com.wadpam.guja.domain.DContact;

/**
 * The DContact domain-object specific mapping methods go here.
 *
 * Generated on 2015-02-10T00:27:00.783+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DContactMapper
  implements Mapper<DContact, Long> {

  private final Supplier supplier;

  public enum Field {
    PARENT("parent"),
    ID("id"),
    ADDRESS1("address1"),
    ADDRESS2("address2"),
    APPARG0("appArg0"),
    BIRTHDAY("birthday"),
    CITY("city"),
    COMPANYNAME("companyName"),
    COUNTRY("country"),
    CREATEDBY("createdBy"),
    CREATEDDATE("createdDate"),
    EMAIL("email"),
    FACEBOOK("facebook"),
    FIRSTNAME("firstName"),
    HOMEPHONE("homePhone"),
    LASTNAME("lastName"),
    LINKEDIN("linkedIn"),
    MOBILEPHONE("mobilePhone"),
    OTHEREMAIL("otherEmail"),
    OTHERPHONE("otherPhone"),
    TAGS("tags"),
    TWITTER("twitter"),
    UNIQUETAG("uniqueTag"),
    UPDATEDBY("updatedBy"),
    UPDATEDDATE("updatedDate"),
    WEBPAGE("webPage"),
    WORKPHONE("workPhone"),
    ZIPCODE("zipCode");

    private final String fieldName;

    Field(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }

  public DContactMapper(Supplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public Long fromKey(Object key) {
    return supplier.toLongKey(key);
  }

  @Override
  public DContact fromReadValue(Object value) {
    final DContact entity = new DContact();

    // set primary key:
    final Object key = supplier.getKey(value, Field.ID.getFieldName());
    entity.setId(supplier.toLongKey(key));

    // set parent key:
    entity.setParent(supplier.getParentKey(value, Field.PARENT.getFieldName()));

    // set all fields:
    entity.setAddress1(supplier.getString(value, Field.ADDRESS1.getFieldName()));
    entity.setAddress2(supplier.getString(value, Field.ADDRESS2.getFieldName()));
    entity.setAppArg0(supplier.getLong(value, Field.APPARG0.getFieldName()));
    entity.setBirthday(supplier.getDate(value, Field.BIRTHDAY.getFieldName()));
    entity.setCity(supplier.getString(value, Field.CITY.getFieldName()));
    entity.setCompanyName(supplier.getString(value, Field.COMPANYNAME.getFieldName()));
    entity.setCountry(supplier.getString(value, Field.COUNTRY.getFieldName()));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setEmail(supplier.getString(value, Field.EMAIL.getFieldName()));
    entity.setFacebook(supplier.getString(value, Field.FACEBOOK.getFieldName()));
    entity.setFirstName(supplier.getString(value, Field.FIRSTNAME.getFieldName()));
    entity.setHomePhone(supplier.getString(value, Field.HOMEPHONE.getFieldName()));
    entity.setLastName(supplier.getString(value, Field.LASTNAME.getFieldName()));
    entity.setLinkedIn(supplier.getString(value, Field.LINKEDIN.getFieldName()));
    entity.setMobilePhone(supplier.getString(value, Field.MOBILEPHONE.getFieldName()));
    entity.setOtherEmail(supplier.getString(value, Field.OTHEREMAIL.getFieldName()));
    entity.setOtherPhone(supplier.getString(value, Field.OTHERPHONE.getFieldName()));
    entity.setTags(supplier.getCollection(value, Field.TAGS.getFieldName()));
    entity.setTwitter(supplier.getString(value, Field.TWITTER.getFieldName()));
    entity.setUniqueTag(supplier.getString(value, Field.UNIQUETAG.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
    entity.setWebPage(supplier.getString(value, Field.WEBPAGE.getFieldName()));
    entity.setWorkPhone(supplier.getString(value, Field.WORKPHONE.getFieldName()));
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
  public Long getId(DContact entity) {
    return entity != null ? entity.getId() : null;
  }

  @Override
  public Object getParentKey(DContact entity) {
    return null != entity ? entity.getParent() : null;
  }

  @Override
  public void setParentKey(DContact entity, Object parentKey) {
    entity.setParent(parentKey);
  }

  @Override
  public void updateEntityPostWrite(DContact entity, Object key, Object value) {
    entity.setId(supplier.toLongKey(key));
    entity.setCreatedBy(supplier.getString(value, Field.CREATEDBY.getFieldName()));
    entity.setCreatedDate(supplier.getDate(value, Field.CREATEDDATE.getFieldName()));
    entity.setUpdatedBy(supplier.getString(value, Field.UPDATEDBY.getFieldName()));
    entity.setUpdatedDate(supplier.getDate(value, Field.UPDATEDDATE.getFieldName()));
}

@Override
  public String getKind() {
    return DContact.class.getSimpleName();
  }

  @Override
  public Object toKey(Object parentKey, Long id) {
    return supplier.toKey(parentKey, getKind(), id);
  }

  @Override
  public Object toWriteValue(DContact entity) {
    final Long id = getId(entity);
    final Object parentKey = getParentKey(entity);
    final Object value = supplier.createWriteValue(parentKey, getKind(), id);

    // set all fields:
    supplier.setString(value, Field.ADDRESS1.getFieldName(), entity.getAddress1());
    supplier.setString(value, Field.ADDRESS2.getFieldName(), entity.getAddress2());
    supplier.setLong(value, Field.APPARG0.getFieldName(), entity.getAppArg0());
    supplier.setDate(value, Field.BIRTHDAY.getFieldName(), entity.getBirthday());
    supplier.setString(value, Field.CITY.getFieldName(), entity.getCity());
    supplier.setString(value, Field.COMPANYNAME.getFieldName(), entity.getCompanyName());
    supplier.setString(value, Field.COUNTRY.getFieldName(), entity.getCountry());
    supplier.setString(value, Field.CREATEDBY.getFieldName(), entity.getCreatedBy());
    supplier.setDate(value, Field.CREATEDDATE.getFieldName(), entity.getCreatedDate());
    supplier.setString(value, Field.EMAIL.getFieldName(), entity.getEmail());
    supplier.setString(value, Field.FACEBOOK.getFieldName(), entity.getFacebook());
    supplier.setString(value, Field.FIRSTNAME.getFieldName(), entity.getFirstName());
    supplier.setString(value, Field.HOMEPHONE.getFieldName(), entity.getHomePhone());
    supplier.setString(value, Field.LASTNAME.getFieldName(), entity.getLastName());
    supplier.setString(value, Field.LINKEDIN.getFieldName(), entity.getLinkedIn());
    supplier.setString(value, Field.MOBILEPHONE.getFieldName(), entity.getMobilePhone());
    supplier.setString(value, Field.OTHEREMAIL.getFieldName(), entity.getOtherEmail());
    supplier.setString(value, Field.OTHERPHONE.getFieldName(), entity.getOtherPhone());
    supplier.setCollection(value, Field.TAGS.getFieldName(), entity.getTags());
    supplier.setString(value, Field.TWITTER.getFieldName(), entity.getTwitter());
    supplier.setString(value, Field.UNIQUETAG.getFieldName(), entity.getUniqueTag());
    supplier.setString(value, Field.UPDATEDBY.getFieldName(), entity.getUpdatedBy());
    supplier.setDate(value, Field.UPDATEDDATE.getFieldName(), entity.getUpdatedDate());
    supplier.setString(value, Field.WEBPAGE.getFieldName(), entity.getWebPage());
    supplier.setString(value, Field.WORKPHONE.getFieldName(), entity.getWorkPhone());
    supplier.setString(value, Field.ZIPCODE.getFieldName(), entity.getZipCode());
    return value;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder extends AbstractEntityBuilder<DContact> {

    protected Builder() {
      super(new DContact());
    }

    public Builder id(Long id) {
      entity.setId(id);
      return this;
    }

    public Builder parent(Object parent) {
      entity.setParent(parent);
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

    public Builder appArg0(Long appArg0) {
      entity.setAppArg0(appArg0);
      return this;
    }

    public Builder birthday(Date birthday) {
      entity.setBirthday(birthday);
      return this;
    }

    public Builder city(String city) {
      entity.setCity(city);
      return this;
    }

    public Builder companyName(String companyName) {
      entity.setCompanyName(companyName);
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

    public Builder email(String email) {
      entity.setEmail(email);
      return this;
    }

    public Builder facebook(String facebook) {
      entity.setFacebook(facebook);
      return this;
    }

    public Builder firstName(String firstName) {
      entity.setFirstName(firstName);
      return this;
    }

    public Builder homePhone(String homePhone) {
      entity.setHomePhone(homePhone);
      return this;
    }

    public Builder lastName(String lastName) {
      entity.setLastName(lastName);
      return this;
    }

    public Builder linkedIn(String linkedIn) {
      entity.setLinkedIn(linkedIn);
      return this;
    }

    public Builder mobilePhone(String mobilePhone) {
      entity.setMobilePhone(mobilePhone);
      return this;
    }

    public Builder otherEmail(String otherEmail) {
      entity.setOtherEmail(otherEmail);
      return this;
    }

    public Builder otherPhone(String otherPhone) {
      entity.setOtherPhone(otherPhone);
      return this;
    }

    public Builder tags(Collection tags) {
      entity.setTags(tags);
      return this;
    }

    public Builder twitter(String twitter) {
      entity.setTwitter(twitter);
      return this;
    }

    public Builder uniqueTag(String uniqueTag) {
      entity.setUniqueTag(uniqueTag);
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

    public Builder webPage(String webPage) {
      entity.setWebPage(webPage);
      return this;
    }

    public Builder workPhone(String workPhone) {
      entity.setWorkPhone(workPhone);
      return this;
    }

    public Builder zipCode(String zipCode) {
      entity.setZipCode(zipCode);
      return this;
    }

  }
}
