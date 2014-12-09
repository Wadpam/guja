package com.wadpam.guja.oauth2.web;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Wrapper for reading the request body multiple times.
 *
 * @author mattiaslevin
 */
public class BodyRequestWrapper extends HttpServletRequestWrapper {

  private final String body;

  public BodyRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;
    try {
      InputStream inputStream = request.getInputStream();
      if (inputStream != null) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead = -1;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
      } else {
        stringBuilder.append("");
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException ex) {
          throw ex;
        }
      }
    }
    body = stringBuilder.toString();
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
    ServletInputStream servletInputStream = new ServletInputStream() {
      public int read() throws IOException {
        return byteArrayInputStream.read();
      }
    };
    return servletInputStream;
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  public String getBody() {
    return this.body;
  }
}
