package com.wadpam.guja.api;

import com.github.zafarkhaja.semver.Version;
import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Check an app version using semantic version check.
 * http://semver.org
 *
 * @author mattiaslevin
 */
public class SemanticVersionCheckPredicate implements VersionCheckResource.VersionCheckPredicate {
  private static final Logger LOGGER = LoggerFactory.getLogger(SemanticVersionCheckPredicate.class);


  // {<device>, <semanticVersionExpression>}
  private final Map<String, String> platformMap;

  @Inject
  public SemanticVersionCheckPredicate(@Named("app.versions.allowed") String allowedVersions) {
    this(parsePropertyMap(checkNotNull(allowedVersions)));
  }

  public SemanticVersionCheckPredicate(Map<String, String> platformMap) {
    this.platformMap = checkNotNull(platformMap);

  }

  private static Map<String, String> parsePropertyMap(String formattedMap) {
    return Splitter.on(",").withKeyValueSeparator("=").split(formattedMap);
  }

  @Override
  public boolean isVersionSupported(String platform, String version) {

    // get the semantic version expression for the platform
    String expression = platformMap.get(platform);
    LOGGER.debug("Check {} against {}", version, expression);

    if (null != expression) {
      return Version.valueOf(version).satisfies(expression);
    } else {
      // If no rule is available for a platform return true as default
      return true;
    }

  }

}
