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

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.common.io.BaseEncoding;
import com.google.common.collect.ImmutableMap;
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.exceptions.NotFoundRestException;
import com.wadpam.guja.oauth2.web.JsonCharacterEncodingReponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Mange GAE blobs.
 *
 * @author mattiaslevin
 */
@Path("api/blob")
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(JsonCharacterEncodingReponseFilter.APPLICATION_JSON_UTF8)
public class GAEBlobResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(GAEBlobResource.class);

  private static final String HEADER_CACHE_CONTROL = "Cache-Control";
  private static final String HEADER_CONTENT_DESPOSITION = "Content-Disposition";
  private static final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();
  private static final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();


  /**
   * Get an upload url to blobstore.
   *
   * @param callback Optional. The url that Blobstore should use as callback url.
   *                 Only set this values if you want a specific handled  method to run
   *                 after a file upload (when default behaviour is not enough)
   * @param request  Optional. If set to true, the callback url will also contain the same
   *                 query parameters as the this quests. Use this to forward parameters to
   *                 the callback method, e.g. access_token.
   * @return a blobstore upload url
   */
  @GET
  @Path("upload")
  public Response getUploadUrl(@QueryParam("callback") String callback,
                               @QueryParam("keepQueryParams") @DefaultValue("false") Boolean keepQueryParam,
                               @Context HttpServletRequest request) {
    LOGGER.debug("Get blobstore upload url");

    if (null == callback) {
      callback = request.getRequestURI();
    }

    // Forward any existing query parameters, e.g. access_token
    if (keepQueryParam) {
      final String queryString = request.getQueryString();
      callback = String.format("%s?%s", callback, null != queryString ? queryString : "");
    }

    return Response.ok(ImmutableMap.builder()
        .put("uploadUrl", blobstoreService.createUploadUrl(callback))
        .build())
        .build();

  }

  /**
   * Default Blobstore callback after successful upload.
   * This method will be called by the Blobstore service after a successful file upload.
   *
   * @return the download url for each of the uploaded files
   */
  @POST
  @Path("upload")
  public Response uploadCallback(@QueryParam("imageSize") Integer imageSize,
                                 @Context HttpServletRequest request,
                                 @Context UriInfo uriInfo) {
    LOGGER.debug("Blobstore upload callback");

    // Get all uploaded blob info records
    Map<String, List<BlobInfo>> blobInfos = blobstoreService.getBlobInfos(request);

    final Map<String, List<String>> response = new TreeMap<>();

    for (Entry<String, List<BlobInfo>> blobInfoEntry : blobInfos.entrySet()) {

      ArrayList<String> servingUrls = new ArrayList<>();
      response.put(blobInfoEntry.getKey(), servingUrls);

      for (BlobInfo blobInfo : blobInfoEntry.getValue()) {

        final BlobKey blobKey = blobInfo.getBlobKey();

        String servingUrl;

        // Serve all blobs of image type smaller then a certain size through the ImageService.
        // This will increase performance and reduce GAE cost (the GAE app will not be involved and the CDN will kick in)
        // All other blobs will be served through this blob resource
        final String contentType = blobInfo.getContentType();
        if (null != contentType && contentType.startsWith("image")
            && null != imageSize && imageSize <= ImagesService.SERVING_SIZES_LIMIT) {

          ServingUrlOptions suo = ServingUrlOptions.Builder.withBlobKey(blobKey);
          LOGGER.debug(" specific image size {}", imageSize);
          suo = suo.imageSize(imageSize);
          servingUrl = imagesService.getServingUrl(suo);

        } else {
          servingUrl = uriInfo.getAbsolutePathBuilder()
              .queryParam("key", blobKey.getKeyString())
              .build()
              .toString();
        }

        servingUrls.add(servingUrl);
      }
    }

    return Response.ok(response).build();
  }


  /**
   * Get a blob.
   *
   * @param key          The blob store key
   * @param maxCacheAge  Optional. Decides the value the Cache-Control header sent back end the response.
   *                     Default value is 1 day.
   *                     Set the 0 if set the cache directive to "no-cache" to avoid the http client
   *                     to do any caching.
   * @param asAttachment Optional. Set the Content-Disposition header to decide if the file
   *                     should be returned as an attachment. Default is false.
   * @return the blob
   */
  @GET
  @Path("{key}")
  public Response getBlob(@PathParam("key") String key,
                          @QueryParam("maxCacheAge") @DefaultValue("86400") int maxCacheAge,
                          @QueryParam("asAttachment") @DefaultValue("false") boolean asAttachment,
                          @Context HttpServletRequest request,
                          @Context HttpServletResponse response) throws IOException {
    checkNotNull(key);
    LOGGER.debug("Get blob with key:{}", key);

    // make sure iOS caches the image (default 1 day)
    if (maxCacheAge > 0) {
      response.setHeader(HEADER_CACHE_CONTROL, String.format("public, max-age=%d", maxCacheAge));
    } else {
      response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
    }

    BlobKey blobKey = new BlobKey(key);
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);

    response.setContentType(blobInfo.getContentType());

    if (asAttachment) {
      // Download the file as attachment
      response.setHeader(HEADER_CONTENT_DESPOSITION, String.format("filename=\"%s\"",
          getEncodeFileName(request.getHeader("User-Agent"), blobInfo.getFilename())));
    }

    blobstoreService.serve(blobKey, response);

    return null; // Need this to avoid warnings
  }

  // Encode header value for Content-Disposition
  private static String getEncodeFileName(String userAgent, String fileName) {
    String encodedFileName = fileName;
    try {
      if (userAgent.contains("MSIE") || userAgent.contains("Opera")) {
        encodedFileName = URLEncoder.encode(fileName, "UTF-8");
      } else {
        encodedFileName = "=?UTF-8?B?" + new String(BaseEncoding.base64().encode(fileName.getBytes("UTF-8"))) + "?=";
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return encodedFileName;
  }


  /**
   * Delete a blob.
   *
   * @param key The blob store key
   * @return 200 if successful
   */
  @DELETE
  @Path("{key}")
  public void deleteBlob(@PathParam("key") String key) throws IOException {
    checkNotNull(key);
    LOGGER.debug("Delete blob with key:{}", key);
    BlobKey blobKey = new BlobKey(key);
    blobstoreService.delete(blobKey);
  }


  /**
   * Get the latest blob by name.
   *
   * @param name     fileName String
   * @param response {@link javax.servlet.http.HttpServletResponse}
   * @throws java.io.IOException
   */
  @GET
  @Path("latest")
  public Response getLatestBlobByName(@QueryParam("name") String name,
                                      @Context HttpServletRequest request,
                                      @Context HttpServletResponse response) throws IOException {
    checkNotNull(name);

    if (null == name || name.isEmpty()) {
      throw new BadRequestRestException("Blob name missing");
    }

    final BlobInfo blobInfo = getLatestBlobResourceByName(name);
    if (null != blobInfo) {
      blobstoreService.serve(blobInfo.getBlobKey(), response);
    } else {
      throw new NotFoundRestException("Blob not found");
    }

    return null; // Need this to avoid warnings

  }

  /**
   * Get the latest BlobResource by name.
   *
   * @param name of resource
   * @return BlobInfo object
   */
  private BlobInfo getLatestBlobResourceByName(String name) {

    Map<Long, BlobInfo> blobsFound = new HashMap<>();
    long latestTimestamp = 0;

    Iterator<BlobInfo> iterator = blobInfoFactory.queryBlobInfos();
    while (iterator.hasNext()) {
      BlobInfo blobInfo = iterator.next();
      if (name.equals(blobInfo.getFilename())) {
        blobsFound.put(blobInfo.getCreation().getTime(), blobInfo);
        latestTimestamp = Math.max(latestTimestamp, blobInfo.getCreation().getTime());
      }
    }

    LOGGER.debug(" found blob resource name {} maxDate : {} ", name, latestTimestamp);

    return blobsFound.get(latestTimestamp);
  }

}
