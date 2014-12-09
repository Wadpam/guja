package com.wadpam.guja.oauth2.providers;

import com.google.appengine.api.utils.SystemProperty;
import com.sun.jersey.spi.resource.Singleton;

import javax.ws.rs.ext.Provider;

/**
 * Provide basic information about the current server environment.
 *
 * @author mattiaslevin
 */
@Singleton
@Provider
public class ServerEnvironmentProvider {

  public boolean isDevEnvironment() {
    return SystemProperty.Environment.Value.Development == SystemProperty.environment.value();
  }

}
