package com.wadpam.guja.json;


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
