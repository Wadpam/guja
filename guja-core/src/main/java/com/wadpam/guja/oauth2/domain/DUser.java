package com.wadpam.guja.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
}
