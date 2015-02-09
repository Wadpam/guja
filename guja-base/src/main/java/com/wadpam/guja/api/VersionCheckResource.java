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


import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import com.wadpam.guja.filter.ProtoWrapperResponseFilter;
import com.wadpam.guja.filter.SkipProtoWrapper;
import com.wadpam.guja.i18n.PropertyFileLocalizationBuilder;
import com.wadpam.guja.web.JsonCharacterEncodingResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Check if an apps version is supported or not.
 *
 * @author mattiaslevin
 */
@Path("api/version")
@RequestScoped
@PermitAll
@Consumes({ProtoWrapperResponseFilter.APPLICATION_X_PROTOBUF, MediaType.APPLICATION_JSON})
@Produces({ProtoWrapperResponseFilter.APPLICATION_X_PROTOBUF, JsonCharacterEncodingResponseFilter.APPLICATION_JSON_UTF8})
public class VersionCheckResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(VersionCheckResource.class);

  private final VersionCheckPredicate predicate;
  private final Provider<PropertyFileLocalizationBuilder> localizationBuilderProvider;
  private final Map<String, String> upgradeUrls;


  @Inject
  public VersionCheckResource(@Named("app.versions.upgradeUrls") String upgradeUrls,
                              VersionCheckPredicate predicate,
                              Provider<PropertyFileLocalizationBuilder> localizationBuilderProvider) {

    this.predicate = predicate;
    this.localizationBuilderProvider = localizationBuilderProvider;
    this.upgradeUrls = parsePropertyMap(upgradeUrls);
  }

  private static Map<String, String> parsePropertyMap(String formattedMap) {
    return Splitter.on(",").withKeyValueSeparator("=").split(formattedMap);
  }

  @GET
  @Path("{version}/check")
  public Response checkVersion(@QueryParam("platform") String platform,
                               @PathParam("version") String version) {
    checkNotNull(platform);
    checkNotNull(version);

    if (predicate.isVersionSupported(platform, version)) {
      LOGGER.debug("Version supported {} {}", version, platform);
      return Response.ok().build();
    } else {

      LOGGER.debug("Version not supported {} {}", version, platform);

      VersionCheckResponse response = new VersionCheckResponse();
      response.setLocalizedMessage(localizationBuilderProvider.get()
          .build()
          .getMessage("updateRequired", "You must upgrade your application"));
      if (null != upgradeUrls && null != upgradeUrls.get(platform)) {
        response.setUrl(upgradeUrls.get(platform));
      }

      return Response.status(Response.Status.GONE)
          .entity(response)
          .build();
    }

  }

  @SkipProtoWrapper
  public static class VersionCheckResponse {
    private String localizedMessage;
    private String url;

    public String getLocalizedMessage() {
      return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
      this.localizedMessage = localizedMessage;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }

  /**
   * Interface for testing if a version is supported.
   */
  public static interface VersionCheckPredicate {
    /**
     * Check if a platform and application combination is supported.
     *
     * @param platform App platform
     * @param version  App version
     * @return true is the version is supported by the backend
     */
    boolean isVersionSupported(String platform, String version);
  }


}
