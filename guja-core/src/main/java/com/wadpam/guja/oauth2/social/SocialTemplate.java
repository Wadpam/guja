/*
 * INSERT COPYRIGHT HERE
 */

package com.wadpam.guja.oauth2.social;

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

import com.google.common.collect.ImmutableMap;
import com.wadpam.guja.oauth2.api.FactoryResource;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;


/**
 * @author sosandstrom
 */
public class SocialTemplate extends NetworkTemplate {
  public static final String BASE_URL_FACEBOOK = "https://graph.facebook.com";

  protected final String access_token;

  public SocialTemplate(String access_token) {
    this(access_token, BASE_URL_FACEBOOK);
  }

  public SocialTemplate(String access_token, String baseUrl) {
    super(baseUrl);
    this.access_token = access_token;
  }

  public static SocialTemplate create(String providerId, String access_token,
                                      String baseUrl, String domain) {
    if (FactoryResource.PROVIDER_ID_FACEBOOK.equals(providerId)) {
      return new SocialTemplate(access_token);
    }

    // TODO Add internal provider

//        if ("itest".equals(providerId) && "itest".equals(domain)) {
//            return new ITestTemplate(access_token);
//        }
    throw new IllegalArgumentException(String.format("No such provider %s.", providerId));
  }

  public SocialProfile getProfile() throws IOException {
    Map<String, Object> props = get(String.format("%s/me", getBaseUrl()), Map.class);
    return parseProfile(props);
  }

  @Override
  public <J> J exchange(String method, String url,
                        Map<String, String> requestHeaders,
                        Object requestBody, Class<J> responseClass) {

    // OAuth access_token
    if (null != access_token) {
      url = String.format("%s%saccess_token=%s",
          url, url.contains("?") ? "&" : "?", access_token);
    }

    return super.exchange(method, url,
        requestHeaders, requestBody, responseClass);
  }

  /**
   * Property names for Facebook - Override to customize
   *
   * @param props
   * @return
   */
  protected SocialProfile parseProfile(Map<String, Object> props) {
    if (!props.containsKey("id")) {
      throw new IllegalArgumentException("No id in profile");
    }
    SocialProfile profile = SocialProfile.with(props)
        .displayName("name")
        .first("first_name")
        .last("last_name")
        .id("id")
        .username("username")
        .profileUrl("link")
        .build();
    profile.setThumbnailUrl(getBaseUrl() + "/" + profile.getId() + "/picture");
    return profile;
  }

  public Map.Entry<String, Integer> extend(String providerId, String clientId, String clientSecret,
                                           String shortLivedToken) {
    if (FactoryResource.PROVIDER_ID_FACEBOOK.equals(providerId)) {
      ImmutableMap<String, String> requestBody = ImmutableMap.of("client_id", clientId,
          "client_secret", clientSecret,
          "grant_type", "fb_exchange_token",
          "fb_exchange_token", shortLivedToken);
      String accessExpires = get(getBaseUrl() + "/oauth/access_token", String.class, requestBody);
      Map<String, String> map = parseQueryString(accessExpires);
      return new AbstractMap.SimpleImmutableEntry<String, Integer>(map.get("access_token"), Integer.valueOf(map.get("expires")));
    }
    return null;
  }
}
