package com.wadpam.guja.web;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * JSONP filter.
 *
 * Will wrap a json response in a Javascript method.
 * The name of the method is selected based on the value of callback parameter name.
 *
 * Take limited credit, partly copied of internet.
 * @author mattias
 */
@Singleton
public class JsonpFilter implements Filter {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonpFilter.class);

  private static final String DEFAULT_CALLBACK_PARAM_NAME = "callback";

  private String callbackParam = DEFAULT_CALLBACK_PARAM_NAME;

  public JsonpFilter() {}

  public JsonpFilter(String callbackParam) {
    this.callbackParam = callbackParam;
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    String _callbackParam = config.getInitParameter("callbackParam");
    if (null != _callbackParam) {
      callbackParam = _callbackParam.replaceAll("[\r\n\\s]", "");
    }
  }

  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    LOGGER.debug("Check jsonp");


    HttpServletRequest httpServletRequest = (HttpServletRequest) req;
    HttpServletResponse httpServletResponse = (HttpServletResponse) res;

    if (isJsonp(httpServletRequest)) {
      LOGGER.debug("JSONP request {}", callbackParam);

      OutputStream out = httpServletResponse.getOutputStream();

      HttpJsonpResponseWrapper wrapper = new HttpJsonpResponseWrapper(httpServletResponse);
      chain.doFilter(req, wrapper);

      out.write((getCallback(httpServletRequest) + "(").getBytes(wrapper.getCharacterEncoding()));
      out.write(wrapper.getData());
      out.write(");".getBytes(wrapper.getCharacterEncoding()));

      wrapper.setContentType("text/javascript;charset=UTF-8");

      out.close();
    } else {
      chain.doFilter(req, res);
    }
  }


  private boolean isJsonp(ServletRequest req) {
    return req.getParameterMap().containsKey(callbackParam);
  }

  private String getCallback(ServletRequest req) {
    return req.getParameterValues(callbackParam)[0];
  }

  public void setCallbackParam(String callbackParam) {
    this.callbackParam = callbackParam;
  }


  public static class HttpJsonpResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream out;
    private int contentLength;
    private String contentType;

    public HttpJsonpResponseWrapper(HttpServletResponse response) {
      super(response);
      out = new ByteArrayOutputStream();
    }


    public byte[] getData() {
      return out.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
      return new ServletOutputStream() {
        private DataOutputStream dos = new DataOutputStream(out);

        public void write(int b) throws IOException {
          dos.write(b);
        }

        public void write(byte[] b) throws IOException {
          dos.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
          dos.write(b, off, len);
        }
      };
    }

    @Override
    public PrintWriter getWriter() {
      return new PrintWriter(getOutputStream(), true);
    }

    @Override
    public void setContentLength(int length) {
      this.contentLength = length;
      super.setContentLength(length);
    }

    @SuppressWarnings("unused")
    public int getContentLength() {
      return contentLength;
    }

    @Override
    public void setContentType(String type) {
      this.contentType = type;
      super.setContentType(type);
    }

    @Override
    public String getContentType() {
      return contentType;
    }

  }

}
