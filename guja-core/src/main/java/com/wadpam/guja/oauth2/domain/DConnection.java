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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Store Oauth2 related authorization information.
 *
 * @author sosandstrom
 * @author mattiaslevin
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"accessToken"}),
    @UniqueConstraint(columnNames = {"refreshToken"})})
public class DConnection extends AbstractLongEntity {

  public static final String ROLE_SEPARATOR = ",";

  @Basic
  private Long userId;

  @Basic
  private String accessToken;

  /**
   * Specify for each provider what this property contains.
   * For Salesforce, it is instance_url
   */
  @Basic
  private String appArg0;

  @Basic
  private String displayName;

  @Basic
  private Date expireTime;

  @Basic
  private String imageUrl;

  @Basic
  private String profileUrl;

  @Basic
  private String providerId;

  @Basic
  private String providerUserId;

  @Basic
  private String refreshToken;

  @Basic
  private String secret;

  /**
   * Comma-separated String, populated by registerFederated()
   */
  @Basic
  private String userRoles;

  @Override
  public String subString() {
    return String.format("%s, accessToken=%s, userId=%s, appArg0=%s, userRoles=%s",
        super.subString(), accessToken, userId, appArg0, userRoles);
  }

  public static ArrayList<String> convertRoles(String from) {
    final ArrayList<String> to = new ArrayList<String>();
    if (null != from) {
      final String[] roles = from.split(ROLE_SEPARATOR);
      for (String r : roles) {
        to.add(r.trim());
      }
    }
    return to;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAppArg0() {
    return appArg0;
  }

  public void setAppArg0(String appArg0) {
    this.appArg0 = appArg0;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Date getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Date expireTime) {
    this.expireTime = expireTime;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public String getProviderUserId() {
    return providerUserId;
  }

  public void setProviderUserId(String providerUserId) {
    this.providerUserId = providerUserId;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(String userRoles) {
    this.userRoles = userRoles;
  }

  public Collection<String> getRoles() {
    return convertRoles(userRoles);
  }

}
