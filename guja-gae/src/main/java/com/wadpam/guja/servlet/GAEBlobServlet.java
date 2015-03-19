package com.wadpam.guja.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import com.wadpam.guja.exceptions.BadRequestRestException;
import com.wadpam.guja.exceptions.NotFoundRestException;
import com.wadpam.guja.web.JsonCharacterEncodingResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Blobs must be served from inside a servlet.
 * It will not work with a Guice-Jersey filter-servlet solution.
 *
 * @author mattias
 */
public class GAEBlobServlet extends HttpServlet {
  private static final Logger LOGGER = LoggerFactory.getLogger(GAEBlobServlet.class);

  private static final String CALLBACK_PARAM = "callback";
  private static final String KEEP_QUERY_PARAM = "keepQueryParam";
  private static final String IMAGE_SIZE_PARAM = "imageSize";
  private static final String KEY_PARAM = "key";
  private static final String MAX_CACHE_AGE = "maxCacheAge";
  private static final String AS_ATTACHMENT_PARAM = "asAttachment";
  private static final String NAME_PARAM = "name";

  private static final String HEADER_CACHE_CONTROL = "Cache-Control";
  private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

  private static final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();
  private static final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();

  /**
   * Url paths supported
   * /api/blob/upload (GET) Generate upload url
   * /api/blob?key=<> (GET) Serve a blob
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getRequestURI().endsWith("upload")) {
      getUploadUrl(req, resp);
    } else if (req.getRequestURI().endsWith("latest")) {
      getLatestByName(req, resp);
    } else if (null != req.getParameter(KEY_PARAM)) {
      serveBlob(req, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Url paths supported
   * /api/blob/upload (POST) Default callback after a successful upload
   */
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getRequestURI().endsWith("upload")) {
      uploadCallback(req, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Url paths supported
   * /api/blob?key=<> (DELETE) Delete a blob based on blob key
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (null != req.getParameter(KEY_PARAM)) {
      deleteBlob(req, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Get an upload URL
   * @param req
   * @param resp
   * @throws ServletException
   * @throws IOException
   */
  private void getUploadUrl(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LOGGER.debug("Get blobstore upload url");

    String callback = req.getParameter(CALLBACK_PARAM);
    if (null == callback) {
      callback = req.getRequestURI();
    }

    String keepQueryParam = req.getParameter(KEEP_QUERY_PARAM);
    // Forward any existing query parameters, e.g. access_token
    if (null != keepQueryParam) {
      final String queryString = req.getQueryString();
      callback = String.format("%s?%s", callback, null != queryString ? queryString : "");
    }

    Map<String, String> response = ImmutableMap.of("uploadUrl", blobstoreService.createUploadUrl(callback));

    PrintWriter out = resp.getWriter();
    resp.setContentType(JsonCharacterEncodingResponseFilter.APPLICATION_JSON_UTF8);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, response);

    out.close();
  }

  private void uploadCallback(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LOGGER.debug("Blobstore upload callback");

    String imageSize = req.getParameter(IMAGE_SIZE_PARAM);

    // Get all uploaded blob info records
    Map<String, List<BlobInfo>> blobInfos = blobstoreService.getBlobInfos(req);

    final Map<String, List<String>> response = new TreeMap<>();
    for (Map.Entry<String, List<BlobInfo>> blobInfoEntry : blobInfos.entrySet()) {

      ArrayList<String> servingUrls = Lists.newArrayList();
      response.put(blobInfoEntry.getKey(), servingUrls);

      for (BlobInfo blobInfo : blobInfoEntry.getValue()) {

        final BlobKey blobKey = blobInfo.getBlobKey();
        LOGGER.debug("blob key {}", blobKey);

        String servingUrl;

        // Serve all blobs of image type smaller then a certain size through the ImageService.
        // This will increase performance and reduce GAE cost (the GAE app will not be involved and the CDN will kick in)
        // All other blobs will be served through this blob resource
        final String contentType = blobInfo.getContentType();
        if (null != contentType && contentType.startsWith("image")
            && null != imageSize && Integer.parseInt(imageSize) <= ImagesService.SERVING_SIZES_LIMIT) {
          LOGGER.debug("serve from image service");

          ServingUrlOptions suo = ServingUrlOptions.Builder.withBlobKey(blobKey);
          LOGGER.debug(" specific image size {}", imageSize);
          suo = suo.imageSize(Integer.parseInt(imageSize));
          servingUrl = imagesService.getServingUrl(suo);

        } else {
          String url = req.getRequestURL().toString();
          url = url.substring(0, url.lastIndexOf("/"));
          servingUrl = url + "?key=" + blobKey.getKeyString();
          LOGGER.debug("serve from blob resource {}", servingUrl);
        }

        servingUrls.add(servingUrl);
      }
    }

    PrintWriter out = resp.getWriter();
    resp.setContentType(JsonCharacterEncodingResponseFilter.APPLICATION_JSON_UTF8);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, response);

    out.close();
  }

  private void serveBlob(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String key = req.getParameter(KEY_PARAM);
    checkNotNull(key);
    LOGGER.debug("Get blob with key {}", key);

    int maxCacheAge = null != req.getParameter(MAX_CACHE_AGE) ? Integer.parseInt(req.getParameter(MAX_CACHE_AGE)) : 0;
    // make sure iOS caches the image (default 1 day)
    if (maxCacheAge > 0) {
      resp.setHeader(HEADER_CACHE_CONTROL, String.format("public, max-age=%d", maxCacheAge));
    } else {
      resp.setHeader(HEADER_CACHE_CONTROL, "no-cache");
    }

    BlobKey blobKey = new BlobKey(key);
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    LOGGER.debug("blob info {} {}", blobInfo, blobInfo.getContentType());

    resp.setContentType(blobInfo.getContentType());

    if (null != req.getParameter(AS_ATTACHMENT_PARAM)) {
      // Download the file as attachment
      resp.setHeader(HEADER_CONTENT_DISPOSITION, String.format("filename=\"%s\"",
          getEncodeFileName(req.getHeader("User-Agent"), blobInfo.getFilename())));
    }

    LOGGER.debug("serve the blob");
    blobstoreService.serve(blobKey, resp);
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

  private void deleteBlob(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String key = req.getParameter(KEY_PARAM);
    checkNotNull(key);
    LOGGER.debug("Delete blob with key:{}", key);
    BlobKey blobKey = new BlobKey(key);
    blobstoreService.delete(blobKey);
  }


  private void getLatestByName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String name = req.getParameter(NAME_PARAM);
    if (null == name || name.isEmpty()) {
      throw new BadRequestRestException("Blob name missing");
    }

    final BlobInfo blobInfo = getLatestBlobResourceByName(name);
    if (null != blobInfo) {
      blobstoreService.serve(blobInfo.getBlobKey(), resp);
    } else {
      throw new NotFoundRestException("Blob not found");
    }

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
