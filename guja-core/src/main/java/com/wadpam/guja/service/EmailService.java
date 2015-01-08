package com.wadpam.guja.service;

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

import java.util.List;

/**
 * Simple service for sending emails.
 * Supports different implementations, e.g. JavaMail or Amazon Simple Email.
 * @mattiaslevin
 */
public interface EmailService {

  /**
   * Send a basic email that only supporting to/from email address.
   * @param toEmail the to email address
   * @param toName the name of the receiver
   * @param subject the subject/title
   * @param body text or html body
   * @param asHtml true if the body should be send as html formatted content
   * @return true if send was successful
   */
  public boolean sendEmail(String toEmail, String toName,
                           String subject,
                           String body, boolean asHtml);


  /**
   * A send email method that support multiple recipients and html body and attachments.
   * @param toAddresses list of to email addresses
   * @param ccAddresses list of cc email addresses
   * @param bccAddresses list of bcc email addresses
   * @param subject the subject/title
   * @param body text or html body
   * @param asHtml true if the body should be send as html formatted content
   * @param attachment email attachment
   * @param filename the name of the attachment
   * @param contentType the content type of the attachment
   * @return true if send was successful
   * */
  public boolean sendEmail(List<String> toAddresses,
                           List<String> ccAddresses,
                           List<String> bccAddresses,
                           String subject,
                           String body, boolean asHtml,
                           byte[] attachment, String filename, String contentType);

}
