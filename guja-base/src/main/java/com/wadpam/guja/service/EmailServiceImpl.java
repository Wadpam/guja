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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * This class is responsible for sending emails.
 * @author mattiaslevin
 */
public class EmailServiceImpl implements EmailService {
    static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    public boolean sendEmail(String toEmail, String fromEmail, String subject, String body) {
        return sendEmail(toEmail, null, fromEmail, null, subject, body, false);
    }

    @Override
    public boolean sendEmail(String toEmail, String toName,
                                    String fromEmail, String fromName,
                                    String subject,
                                    String body, boolean asHtml) {

        LOGGER.debug("Send email to:{}, subject:{}", toEmail, subject);

        final Session session = Session.getDefaultInstance(new Properties(), null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, fromName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toName));
            msg.setSubject(subject);

            // Check if plain text or multi-part message
            if (!asHtml) {
                // Plain text
                msg.setText(body);
            } else {
                // Multi-part message
                final Multipart mp = new MimeMultipart();

                // Add body as html
                final MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(body, "text/html");
                mp.addBodyPart(htmlPart);
                msg.setContent(mp);
            }

            msg.setText(body);
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            // Catch all exceptions and just log an error, do not interrupt flow
            LOGGER.error("Not possible to send email with reason:{}", e.getMessage());
            return false;
        }

    }


    @Override
    public boolean sendEmail(String fromAddress, String fromName,
                             List<String> toAddresses,
                             List<String> ccAddresses,
                             List<String> bccAddresses,
                             String subject,
                             String body, boolean asHtml,
                             byte[] attachment, String filename, String contentType) {
        LOGGER.info("Send email to:{}, subject:{}", toAddresses, subject);

        final Session session = Session.getDefaultInstance(new Properties(), null);
        try {
            // Build message
            final Message msg = new MimeMessage(session);

            // Set from address
            final InternetAddress from = new InternetAddress(fromAddress, fromName);
            msg.setFrom(from);
            // Set to address
            if (toAddresses != null) {
                for (String toAddress : toAddresses) {
                    final InternetAddress to = new InternetAddress(toAddress, null);
                    msg.addRecipient(Message.RecipientType.TO, to);
                }
            }

            if (null == toAddresses && null == ccAddresses && null == bccAddresses) {
                LOGGER.warn("One of to, cc or bcc address must be provided to send email");
                return false;
            }

            // Set cc address
            if (ccAddresses != null) {
                for (String ccAddress : ccAddresses) {
                    final InternetAddress cc = new InternetAddress(ccAddress, null);
                    msg.addRecipient(Message.RecipientType.CC, cc);
                }
            }
            // Set bcc address
            if (bccAddresses != null) {
                for (String bccAddress : bccAddresses) {
                    final InternetAddress bcc = new InternetAddress(bccAddress, null);
                    msg.addRecipient(Message.RecipientType.BCC, bcc);
                }
            }
            // Subject
            msg.setSubject(subject);

            // Check if plain text or multi-part message
            if (!asHtml) {
                // Plain text
                msg.setText(body);
            } else {
                // Multi-part message
                final Multipart mp = new MimeMultipart();

                // Add body as html
                final MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(body, "text/html");
                mp.addBodyPart(htmlPart);

                // Add attachments if any
                if (null != attachment) {
                    LOGGER.debug("Add attachment to email");
                    final MimeBodyPart attachmentPart = createBodyPart(attachment, contentType, filename);
                    mp.addBodyPart(attachmentPart);
                }

                msg.setContent(mp);
            }

            msg.saveChanges();
            Transport.send(msg);
            return true;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.warn("Cannot create InternetAddress when sending email:{}", ex);
            return false;
        } catch (MessagingException me) {
            LOGGER.warn("Cannot send email with reason:{}", me);
            return false;
        }
    }

    // Build attachment part
    private static MimeBodyPart createBodyPart(byte[] data, String type, String filename) throws MessagingException {
        final MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setFileName(filename);
        ByteArrayDataSource source = new ByteArrayDataSource(data, type);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setHeader("Content-ID", createContentID(filename));
        attachmentPart.setDisposition(MimeBodyPart.INLINE);
        return attachmentPart;
    }

    private static String createContentID(String filename) {
        return String.format("%s_no-reply@bassac.se", filename); // TODO Make more generic
    }

}
