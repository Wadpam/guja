/*
 * INSERT COPYRIGHT HERE
 */

package com.wadpam.guja.oauth2.social;

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

import java.util.Map;

/**
 * @author sosandstrom
 */
public class SocialProfile {
  private String displayName;
  private String firstName;
  private String lastName;
  private String email;
  private String id;
  private String username;
  private String profileUrl;
  private String thumbnailUrl;

  private Map<String, Object> props = null;

  public SocialProfile() {
  }

  protected SocialProfile(Map<String, Object> props) {
    this.props = props;
  }

  public static com.wadpam.guja.oauth2.social.SocialProfile with(Map<String, Object> props) {
    return new com.wadpam.guja.oauth2.social.SocialProfile(props);
  }

  public com.wadpam.guja.oauth2.social.SocialProfile build() {
    props = null;
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile displayName(String propertyName) {
    this.displayName = (String) props.get(propertyName);
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile first(String propertyName) {
    this.firstName = (String) props.get(propertyName);
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile last(String propertyName) {
    this.lastName = (String) props.get(propertyName);
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile id(String propertyName) {
    this.id = (String) props.get(propertyName);
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile email(String propertyName) {
    this.email = (String) props.get(propertyName);
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile username(String propertyName) {
    this.username = (String) props.get(propertyName);
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile profileUrl(String propertyName) {
    this.profileUrl = (String) props.get(propertyName);
    return this;
  }

  public com.wadpam.guja.oauth2.social.SocialProfile thumbnailUrl(String propertyName) {
    this.thumbnailUrl = (String) props.get(propertyName);
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getEmail() {
    return email;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setProps(Map<String, Object> props) {
    this.props = props;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
}
