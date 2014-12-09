package com.wadpam.guja.api;


import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;


/**
 * A stupid monitor that answers any requests with the app name, version and current server time.
 * Can be used to monitoring and making sure the server is up and running.
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
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response ping() {
        LOGGER.debug("Ping");

        return Response.ok(ImmutableMap.builder()
                .put("name", appName)
                .put("version", appVersion)
                .put("date", new Date().toString())
                .build())
                .build();
    }

}
