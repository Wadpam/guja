package com.wadpam.guja.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User domain object.
 * Holds the users account information such as password.
 *
 * @author mattiaslevin
 */
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
   * State of the user account.
   * 0 - unverified; user has not verified the email yet
   * 1 - verified; user has verified the email
   * 2 - locked; account is locked
   */
  private Integer state;

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

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }


}
