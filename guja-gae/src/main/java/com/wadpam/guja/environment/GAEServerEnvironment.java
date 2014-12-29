package com.wadpam.guja.environment;

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

import com.google.appengine.api.utils.SystemProperty;
import com.sun.corba.se.spi.activation.Server;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Provide basic information about the current GAE server environment.
 *
 * @author mattiaslevin
 */
@Singleton
public class GAEServerEnvironment implements ServerEnvironment {

  @Override
  public boolean isDevEnvironment() {
    return SystemProperty.Environment.Value.Development == SystemProperty.environment.value();
  }

}
