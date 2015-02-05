package com.wadpam.guja.api;

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



import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.wadpam.guja.web.JsonCharacterEncodingResponseFilter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


/**
 * A stupid monitor that answers any requests with the app name, version and current server time.
 * Can be used to monitoring and making sure the server is up and running.
 *
 * @author mattiaslevin
 */
@Path("api/monitor")
@Singleton
@PermitAll
public class MonitorResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(MonitorResource.class);

  private String appName = "missing";
  private String appVersion = "missing";

  @Inject
  public MonitorResource(@Named("app.name") String appName, @Named("app.version") String appVersion) {
    this.appName = appName;
    this.appVersion = appVersion;
  }

  @GET
  @Produces(JsonCharacterEncodingResponseFilter.APPLICATION_JSON_UTF8)
  public Response ping() {
    LOGGER.debug("Ping");

    return Response.ok(ImmutableMap.builder()
        .put("name", appName)
        .put("version", appVersion)
        .put("date", DateTime.now().toString())
        .put("timestamp", DateTime.now().getMillis() / 1000)
        .build())
        .build();
  }

}
