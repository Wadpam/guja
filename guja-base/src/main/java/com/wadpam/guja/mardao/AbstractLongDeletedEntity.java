package com.wadpam.guja.mardao;

import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Add a deleted by and date to the entity.
 *
 * @author mattiaslevin
 */
@Entity
public abstract class AbstractLongDeletedEntity extends AbstractLongEntity {

  public AbstractLongDeletedEntity() {
  }

  @Basic
  private String deletedBy;

  @Basic
  private Date deletedDate;

  public String getDeletedBy() {
    return deletedBy;
  }

  public void setDeletedBy(String deletedBy) {
    this.deletedBy = deletedBy;
  }

  public Date getDeletedDate() {
    return deletedDate;
  }

  public void setDeletedDate(Date deletedDate) {
    this.deletedDate = deletedDate;
  }
}
