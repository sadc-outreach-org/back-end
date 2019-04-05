package backend.Utility;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;

@Service
public class EmailServiceService {

    private String signUpLink       = "http://34.73.221.154:5000/";
    private String signUpSuccessul  = "Thank you for signing up for SADC-Trinity-2019. Please visit " + signUpLink + " to log in";


    private String from = "sadctrinity@gmail.com";

    private EmailServiceService() {}

    @Autowired
    private EmailService emailService;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
    }

    public void sendEmail(String to, String subject, String text) throws AddressException {
        final Email email = DefaultEmail.builder()
             .from(new InternetAddress(from))
             .replyTo(new InternetAddress(to))
             .to(Lists.newArrayList(new InternetAddress(to)))
             .subject(subject)
             .body(text)
             .encoding("UTF-8").build();
        emailService.send(email);
     }

     public void sendSignUpEmail(String to) throws AddressException
     {
        String subject = "Signup successful for SADC-Trinity";
        sendEmail(to, subject, signUpSuccessul);

     }
}