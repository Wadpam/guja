package com.wadpam.guja.config;

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

import com.google.inject.persist.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom Date: 1/19/14 Time: 8:16 PM
 */
public class MardaoGuiceUnitOfWork implements UnitOfWork {
  static final Logger LOGGER = LoggerFactory.getLogger(MardaoGuiceUnitOfWork.class);

  public MardaoGuiceUnitOfWork() {
    LOGGER.debug("<init>");
  }

  @Override
  public void begin() {
    LOGGER.debug("begin()");
  }

  @Override
  public void end() {
    LOGGER.debug("end()");
  }

}
