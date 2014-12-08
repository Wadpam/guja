package com.wadpam.guja.api;

import com.github.zafarkhaja.semver.Version;

import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * Check an app version using semantic version check.
 * http://semver.org
 * @author mattiaslevin
 */
public class SemanticVersionCheckPredicate implements VersionCheckResource.VersionCheckPredicate {

    // {<device>, <semanticVersionExpression>}
    private final Map<String, String> platformMap;

    public SemanticVersionCheckPredicate(Map<String, String> platformMap) {
        this.platformMap = checkNotNull(platformMap);
    }

    @Override
    public Optional<Boolean> isVersionSupported(String platform, String version) {

        // get the semantic version expression for the platform
        String expression = platformMap.get(platform);
        if (null != expression) {
            return Optional.of(Version.valueOf(version).satisfies(expression));
        } else {
            return Optional.of(false);
        }

    }

}
