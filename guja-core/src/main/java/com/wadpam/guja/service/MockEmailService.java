package com.wadpam.guja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A mock email service that prints to the log file.
 * @author mattiaslevin
 */
public class MockEmailService implements EmailService {
  static final Logger LOGGER = LoggerFactory.getLogger(MockEmailService.class);


  @Override
  public boolean sendEmail(String toEmail, String toName, String subject, String body, boolean asHtml) {
    LOGGER.info("to: {} subject {}", toEmail, subject);
    return true;
  }

  @Override
  public boolean sendEmail(List<String> toAddresses, List<String> ccAddresses, List<String> bccAddresses, String subject, String body, boolean asHtml, byte[] attachment, String filename, String contentType) {
    LOGGER.info("to: {} subject {}", toAddresses, subject);
    return true;
  }

}
