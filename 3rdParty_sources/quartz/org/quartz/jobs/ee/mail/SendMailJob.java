/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */
package org.quartz.jobs.ee.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>
 * A Job which sends an e-mail with the configured content to the configured
 * recipient.
 * </p>
 * 
 * @author James House
 */
public class SendMailJob implements Job {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * The host name of the smtp server. REQUIRED.
     */
    public static final String PROP_SMTP_HOST = "smtp_host";

    /**
     * The e-mail address to send the mail to. REQUIRED.
     */
    public static final String PROP_RECIPIENT = "recipient";

    /**
     * The e-mail address to cc the mail to. Optional.
     */
    public static final String PROP_CC_RECIPIENT = "cc_recipient";

    /**
     * The e-mail address to claim the mail is from. REQUIRED.
     */
    public static final String PROP_SENDER = "sender";

    /**
     * The e-mail address the message should say to reply to. Optional.
     */
    public static final String PROP_REPLY_TO = "reply_to";

    /**
     * The subject to place on the e-mail. REQUIRED.
     */
    public static final String PROP_SUBJECT = "subject";

    /**
     * The e-mail message body. REQUIRED.
     */
    public static final String PROP_MESSAGE = "message";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        JobDataMap data = context.getJobDetail().getJobDataMap();

        String smtpHost = data.getString(PROP_SMTP_HOST);
        String to = data.getString(PROP_RECIPIENT);
        String cc = data.getString(PROP_CC_RECIPIENT);
        String from = data.getString(PROP_SENDER);
        String replyTo = data.getString(PROP_REPLY_TO);
        String subject = data.getString(PROP_SUBJECT);
        String message = data.getString(PROP_MESSAGE);

        if (smtpHost == null || smtpHost.trim().length() == 0)
                throw new IllegalArgumentException(
                        "PROP_SMTP_HOST not specified.");
        if (to == null || to.trim().length() == 0)
                throw new IllegalArgumentException(
                        "PROP_RECIPIENT not specified.");
        if (from == null || from.trim().length() == 0)
                throw new IllegalArgumentException("PROP_SENDER not specified.");
        if (subject == null || subject.trim().length() == 0)
                throw new IllegalArgumentException(
                        "PROP_SUBJECT not specified.");
        if (message == null || message.trim().length() == 0)
                throw new IllegalArgumentException(
                        "PROP_MESSAGE not specified.");

        if (cc != null && cc.trim().length() == 0) cc = null;

        if (replyTo != null && replyTo.trim().length() == 0) replyTo = null;

        String mailDesc = "'" + subject + "' to: " + to;

        getLog().info("Sending message " + mailDesc);

        try {
            sendMail(smtpHost, to, cc, from, replyTo, subject, message);
        } catch (MessagingException e) {
            throw new JobExecutionException("Unable to send mail: " + mailDesc,
                    e, false);
        }

    }

    private static Log getLog() {
        return LogFactory.getLog(SendMailJob.class);
    }

    private void sendMail(String smtpHost, String to, String cc, String from,
            String replyTo, String subject, String message)
            throws MessagingException {
        MimeMessage mimeMessage = prepareMimeMessage(smtpHost, to, cc, from,
                replyTo, subject);
        mimeMessage.setText(message);
        Transport.send(mimeMessage);
    }

    private MimeMessage prepareMimeMessage(String smtpHost, String to,
            String cc, String from, String replyTo, String subject)
            throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        Session session = Session.getDefaultInstance(properties, null);

        MimeMessage mimeMessage = new MimeMessage(session);

        Address[] toAddresses = InternetAddress.parse(to);
        mimeMessage.setRecipients(Message.RecipientType.TO, toAddresses);

        if (cc != null) {
            Address[] ccAddresses = InternetAddress.parse(cc);
            mimeMessage.setRecipients(Message.RecipientType.CC, ccAddresses);
        }

        mimeMessage.setFrom(new InternetAddress(from));
        if (replyTo != null)
                mimeMessage
                        .setReplyTo(new InternetAddress[]{new InternetAddress(
                                replyTo)});
        mimeMessage.setSubject(subject);
        mimeMessage.setSentDate(new Date());

        return mimeMessage;
    }

}
