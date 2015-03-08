package com.wadpam.guja.domain;

import net.sf.mardao.core.Cached;
import net.sf.mardao.core.Parent;
import net.sf.mardao.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.Date;

/**
 * A contact representation.
 *
 * A contacts may have a parent-child relationship, e.g. to create organisations or hierarchies.
 *
 * @author mattiaslevin
 */
@Cached
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"uniqueTag"}))
public class DContact extends AbstractLongEntity {

  @Parent(kind = "DContact")
  @Basic
  private Object parent;

  /* Meta data */
  /**
   * A unique tag that can be used to directly reach the contact without knowing its key.
   */
  @Basic
  private String uniqueTag;

  /**
   * Collection or tags or categories associated with the user.
   * Can be used to search users and filter organisations.
   */
  @Basic
  private Collection<String> tags;

  /**
   * Provide a primary custom index user for sorting results.
   * E.g. Capitalized first letter, sort by group.
   */
  @Basic
  private String primaryCustomIndex;


  /**
   * Secondary custom index.
   */
  @Basic
  private String secondaryCustomIndex;


  /* Contact properties */

  /**
   * Application specific property. Could be a loose relation to a user entity or a company.
   */
  @Basic
  private Long appArg0;

  /**
   * First name.
   */
  @Basic
  private String firstName;

  /**
   * Last name.
   */
  @Basic
  private String lastName;

  /**
   * If this is a company contact, the company name.
   * Project specific how this is used.
   */
  @Basic
  private String companyName;

  /**
   * Birthday.
   */
  @Basic
  private Date birthday;

  /**
   * Home phone number.
   */
  @Basic
  private String homePhone;

  /**
   * Work phone number.
   */
  @Basic
  private String workPhone;

  /**
   * Mobile phone number.
   */
  @Basic
  private String mobilePhone;

  /**
   * Other phone number
   */
  @Basic
  private String otherPhone;

  /**
   * Email address.
   */
  @Basic
  private String email;

  /**
   * Other email, e.g. work or private email address.
   */
  @Basic
  private String otherEmail;

  /**
   * Home page link.
   */
  @Basic
  private String webPage;

  /**
   * Facebook link.
   */
  @Basic
  private String facebook;

  /**
   * Twitter link.
   */
  @Basic
  private String twitter;

  /**
   * LinkedIn link.
   */
  @Basic
  private String linkedIn;

  /**
   * Address line 1.
   */
  @Basic
  private String address1;

  /**
   * Address line 2.
   * Project and country specific use.
   */
  @Basic
  private String address2;

  /**
   * ZIP code.
   */
  @Basic
  private String zipCode;

  /**
   * City
   */
  @Basic
  private String city;

  /**
   * Country.
   */
  @Basic
  private String country;

  /**
   * Latitude
   */
  @Basic
  private Float latitude;

  /**
   * Longitude
   */
  @Basic
  private Float longitude;


  /* Setters and getters */

  public Object getParent() {
    return parent;
  }

  public void setParent(Object parent) {
    this.parent = parent;
  }

  public String getUniqueTag() {
    return uniqueTag;
  }

  public void setUniqueTag(String uniqueTag) {
    this.uniqueTag = uniqueTag;
  }

  public Collection<String> getTags() {
    return tags;
  }

  public void setTags(Collection<String> tags) {
    this.tags = tags;
  }

  public String getPrimaryCustomIndex() {
    return primaryCustomIndex;
  }

  public void setPrimaryCustomIndex(String primaryCustomIndex) {
    this.primaryCustomIndex = primaryCustomIndex;
  }

  public String getSecondaryCustomIndex() {
    return secondaryCustomIndex;
  }

  public void setSecondaryCustomIndex(String secondaryCustomIndex) {
    this.secondaryCustomIndex = secondaryCustomIndex;
  }

  public Long getAppArg0() {
    return appArg0;
  }

  public void setAppArg0(Long appArg0) {
    this.appArg0 = appArg0;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getOtherPhone() {
    return otherPhone;
  }

  public void setOtherPhone(String otherPhone) {
    this.otherPhone = otherPhone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getOtherEmail() {
    return otherEmail;
  }

  public void setOtherEmail(String otherEmail) {
    this.otherEmail = otherEmail;
  }

  public String getWebPage() {
    return webPage;
  }

  public void setWebPage(String webPage) {
    this.webPage = webPage;
  }

  public String getFacebook() {
    return facebook;
  }

  public void setFacebook(String facebook) {
    this.facebook = facebook;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public String getLinkedIn() {
    return linkedIn;
  }

  public void setLinkedIn(String linkedIn) {
    this.linkedIn = linkedIn;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }
}
