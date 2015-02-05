package com.wadpam.guja.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Automatically append UTF-8 character encoding to the application/json content type.
 *
 * @author mattiaslevin
 */
public class JsonCharacterEncodingResponseFilter implements Filter {

  public static final String APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON + ";charset=utf-8";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Do nothing
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    final HttpServletResponse resp = new HttpServletResponseWrapper((HttpServletResponse)response) {
      public void setContentType(String contentType) {
        if(null != contentType && contentType.toLowerCase().startsWith("application/json")) {
          super.setContentType("application/json;charset=UTF-8");
        } else {
          super.setContentType(contentType);
        }
      }
    };

    chain.doFilter(request, resp);
  }

  @Override
  public void destroy() {
    // Do nothing
  }

}
