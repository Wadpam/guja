package com.wadpam.guja.oauth2.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.mardao.core.Cached;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collection;

/**
 * User domain object.
 * Holds the users account information such as password.
 *
 * @author mattiaslevin
 */
@Cached
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class DUser extends DOAuth2User {

  /**
   * A unique user name
   */
  @Basic
  private String username;

  /**
   * User password (hashed)
   */
  @Basic
  private String password;

  /**
   * User users marked as friends.
   * Can be used to create associations between users. The exact meaning of the relations is up to the domain.
   */
  @Basic
  private Collection<Long> friends;

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
   * Phone number 1.
   * Project and country specific use.
   */
  @Basic
  private String phoneNumber1;

  /**
   * Phone number 2.
   * Project and country specific use.
   */
  @Basic
  private String phoneNumber2;

  /**
   * Birth related information.
   * Project specific user, e.g. birth year.
   */
  @Basic
  private String birthInfo;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonProperty("password")
  public void setPassword(String password) {
    this.password = password;
  }

  public Collection<Long> getFriends() {
    return friends;
  }

  public void setFriends(Collection<Long> friends) {
    this.friends = friends;
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

  public String getPhoneNumber1() {
    return phoneNumber1;
  }

  public void setPhoneNumber1(String phoneNumber1) {
    this.phoneNumber1 = phoneNumber1;
  }

  public String getPhoneNumber2() {
    return phoneNumber2;
  }

  public void setPhoneNumber2(String phoneNumber2) {
    this.phoneNumber2 = phoneNumber2;
  }

  public String getBirthInfo() {
    return birthInfo;
  }

  public void setBirthInfo(String birthInfo) {
    this.birthInfo = birthInfo;
  }
}
