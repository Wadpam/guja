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

  // TODO Missing a lot of properties - name, address etc


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
