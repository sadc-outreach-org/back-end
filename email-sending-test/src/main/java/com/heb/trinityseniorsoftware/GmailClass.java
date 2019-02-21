package com.heb.trinityseniorsoftware;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GmailClass 
{
    private GmailClass(){
    }

    private static MimeMessage createEmail(String to, String from, String subject, String bodyText) 
    	throws MessagingException {
	    	Properties props = new Properties();
	    	Session session = Session.getDefaultInstance(props, null);

	    	MimeMessage email = new MimeMessage(session);
	    	email.setFrom(new InternetAddress(from));
	    	email.addRecipient(javax.mail.Message.RecipientType.TO,
	    		new InternetAddress(to));
	    	email.setSubject(subject);
	    	email.setText(bodyText);
	    	return email;
    }

    public static Message createMessageWithEmail(MimeMessage emailContent) 
    	throws MessagingException, IOException {
    		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    		emailContent.writeTo(buffer);
    		String encodedEmail = Base64.encodeBase64URLSafeString(buffer.toByteArray());
    		Message message = new Message();
    		message.setRaw(encodedEmail);
    		return message;
    	}

    public static void Send(Gmail service, String recipientEmail, String fromEmail, String title, String message)
    	throws MessagingException, IOException {
    		Message m = createMessageWithEmail(createEmail(recipientEmail, fromEmail, title, message));
    		service.users().messages().send("me", m).execute();
    	}

}
