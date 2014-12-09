/*
 * INSERT COPYRIGHT HERE
 */

package com.wadpam.guja.oauth2.domain;

import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.util.Collection;

/**
 * Basic user object to support oauth2.
 *
 * @author sosandstrom
 * @author mattiaslevin
 */
@Entity
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
