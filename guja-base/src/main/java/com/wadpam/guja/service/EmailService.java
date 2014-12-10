package com.wadpam.guja.service;

import java.util.List;

/**
 * Simple service for sending emails.
 * @mattiaslevin
 */
public interface EmailService {

  /**
   * Send a basic email that only supporting to/from email address.
   * @param toEmail the to email address
   * @param toName the name of the receiver
   * @param fromEmail the from email address.
   *                  Please note the restrictions on GAE when setting the from email.
   *                  https://developers.google.com/appengine/docs/java/mail/usingjavamail#Senders_and_Recipients
   * @param fromName the name of the sender
   * @param subject the subject/title
   * @param body text or html body
   * @param asHtml true if the body should be send as html formatted content
   * @return true if send was successful
   */
  public boolean sendEmail(String toEmail, String toName,
                           String fromEmail, String fromName,
                           String subject,
                           String body, boolean asHtml);


  /**
   * A send email method that support multiple recipients and html body and attachments.
   * @param fromAddress the from email address
   * @param fromName the name of the sender
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
  public boolean sendEmail(String fromAddress, String fromName,
                           List<String> toAddresses,
                           List<String> ccAddresses,
                           List<String> bccAddresses,
                           String subject,
                           String body, boolean asHtml,
                           byte[] attachment, String filename, String contentType);

}
