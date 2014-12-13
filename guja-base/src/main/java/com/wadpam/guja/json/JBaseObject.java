package com.wadpam.guja.json;

/*
 * #%L
 * guja-base
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


import java.io.Serializable;

/**
 * A base JSON class containing common parameters.
 *
 * @author Mattias & Ola & Sopea
 */
public class JBaseObject implements Serializable {
  /**
   * Unique id for this Entity in the database
   */
  private String id;

  /**
   * Set by mardao to whom this entity was created by
   */
  private String createdBy;

  /**
   * Milliseconds since 1970 when this Entity was created in the database
   */
  private Long createdDate;

  /**
   * Possible states are DELETED (-1), PENDING (0), ACTIVE (1) and REDEEMED (2)
   */
  // TODO Not supported by the Dao
  private Long state;

  /**
   * Set by mardao to whom this entity was updated by last time
   */
  private String updatedBy;

  /**
   * Milliseconds since 1970 when this Entity was last updated in the database
   */
  private Long updatedDate;


  public JBaseObject() {
  }

  public JBaseObject(String id, Long createdDate, Long state, Long updatedDate) {
    this.id = id;
    this.createdDate = createdDate;
    this.state = state;
    this.updatedDate = updatedDate;
  }

  public JBaseObject(String id, String createdBy, Long createdDate, Long state, String updatedBy, Long updatedDate) {
    this.id = id;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.state = state;
    this.updatedBy = updatedBy;
    this.updatedDate = updatedDate;
  }

  @Override
  public String toString() {
    return String.format("%s{id:%s, updatedDate:%s, %s}", getClass().getSimpleName(), id, updatedDate, subString());
  }

  protected String subString() {
    return "";
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public Long getState() {
    return state;
  }

  public void setState(Long state) {
    this.state = state;
  }

  public Long getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Long updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

}
