package com.wadpam.guja.exceptions;

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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Base class for all rest exceptions.
 *
 * @author mattiaslevin
 */
public class RestException extends RuntimeException {
  private static final Logger LOGGER = LoggerFactory.getLogger(RestException.class);


  private Response.Status status;
  private Map<String, String> json;


  public RestException(Response.Status status) {
    this.status = status;
  }

  public RestException(Response.Status status, String message) {
    super(message);
    this.status = status;
  }

  public RestException(Response.Status status, Map<String, String> json) {
    this.status = status;
    this.json = json;
  }


  public Response.Status getStatus() {
    return status;
  }

  public Map<String, String> getJson() {
    return json;
  }

}
