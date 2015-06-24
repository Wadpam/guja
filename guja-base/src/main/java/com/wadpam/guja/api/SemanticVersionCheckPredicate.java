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
    this(parsePropertyMap(allowedVersions));
  }

  public SemanticVersionCheckPredicate(Map<String, String> platformMap) {
    this.platformMap = checkNotNull(platformMap);

  }

  private static Map<String, String> parsePropertyMap(String formattedMap) {
    return Splitter.on(",").withKeyValueSeparator(":").split(formattedMap);
  }

  @Override
  public boolean isVersionSupported(String platform, String version) {
    checkNotNull(platform);
    checkNotNull(version);

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
