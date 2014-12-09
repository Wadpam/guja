package com.wadpam.guja.api;


import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import com.wadpam.guja.i18n.Localization;
import com.wadpam.guja.i18n.PropertyFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
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
public class VersionCheckResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(VersionCheckResource.class);

  private final VersionCheckPredicate predicate;
  private final Localization localization;
  private final Map<String, String> upgradeUrls;


  @Inject
  public VersionCheckResource(@Named("app.versions.upgradeUrls") String upgradeUrls,
                              VersionCheckPredicate predicate,
                              @PropertyFile Localization localization) {
    this.predicate = checkNotNull(predicate);
    this.localization = checkNotNull(localization);
    this.upgradeUrls = parsePropertyMap(checkNotNull(upgradeUrls));

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
      ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder()
          .put("localizedMessage", localization.getMessage("updateRequired", "You must upgrade your application"));

      if (null != upgradeUrls && null != upgradeUrls.get(platform)) {
        builder.put("url", upgradeUrls.get(platform));
      }

      return Response.status(Response.Status.GONE)
          .entity(builder.build())
          .build();
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
