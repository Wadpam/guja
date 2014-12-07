package com.wadpam.guja.guice;

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
