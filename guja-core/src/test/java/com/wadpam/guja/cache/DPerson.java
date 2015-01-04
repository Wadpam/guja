package com.wadpam.guja.cache;


import net.sf.mardao.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Dao for unit testing.
 *
 * @author mattiaslevin
 */
@Entity
public class DPerson extends AbstractLongEntity {

  @Basic
  private String firstName;

  @Basic
  private String lastName;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
