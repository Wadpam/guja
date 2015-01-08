/*
 * INSERT COPYRIGHT HERE
 */

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

import net.sf.mardao.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collection;

/**
 * Basic user object to support oauth2.
 *
 * @author sosandstrom
 * @author mattiaslevin
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class DOAuth2User extends AbstractLongEntity {

  public static final int UNVERIFIED_STATE = 0;
  public static final int ACTIVE_STATE = 1;
  public static final int LOCKED_STATE = 2;

  @Basic
  private String displayName;

  @Basic
  private String email;

  @Basic
  private String profileLink;

  @Basic
  private Collection<String> roles;

  /**
   * State of the user account.
   * 0 - unverified; user has not verified the email yet
   * 1 - verified; user has verified the email
   * 2 - locked; account is locked
   */
  @Basic
  private Integer state;

  @Basic
  private String thumbnailUrl;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Collection<String> getRoles() {
    return roles;
  }

  public void setRoles(Collection<String> roles) {
    this.roles = roles;
  }

  public String getProfileLink() {
    return profileLink;
  }

  public void setProfileLink(String profileLink) {
    this.profileLink = profileLink;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }


}
