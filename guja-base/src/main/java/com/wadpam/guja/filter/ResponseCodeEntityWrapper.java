package com.wadpam.guja.filter;

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

/**
 * Wrap an entity response object with the http response code.
 *
 * @author mattiaslevin
 */
public class ResponseCodeEntityWrapper<T> {

  private int responseCode;

  private T entity;

  public ResponseCodeEntityWrapper(int responseCode, T entity) {
    this.responseCode = responseCode;
    this.entity = entity;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public T getEntity() {
    return entity;
  }
}
