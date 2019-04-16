package backend.Utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import backend.model.Application;


@Service
public class EmailService {

    private final String HOME               = "http://34.73.221.154:5000/";

    private final String SIGN_UP            = "Thank you for expressing interest in working at H-E-B. If this is your first time applying, please visit <a href=" 
                                    + HOME + ">H-E-B Recruiting</a> to reset your password.";
    private final String SUBMIT_GIT_LINK    = "You have submitted a link to a Git Repository. Log into  <a href=" + HOME + ">H-E-B Recruiting</a> to view."; 
    private final String CARRIAGE_RETURN    = "<br><br>";

    @Autowired
    private JavaMailSender sender;

    private String from = "sadctrinity@gmail.com";

    private EmailService() {}

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
    }

    private void sendEmail(String to, String subject, String text) {
        MimeMessage message         = sender.createMimeMessage();
        MimeMessageHelper helper    = new MimeMessageHelper(message);
        try {
        helper.setFrom(new InternetAddress(from));
        helper.setTo(new InternetAddress(to));
        helper.setReplyTo(new InternetAddress(to));
        helper.setSubject(subject);
        helper.setText(text, true);
        }
        catch (MessagingException ex)
        {
            ex.printStackTrace();
        }
        sender.send(message);
     }

    public void sendSignUpEmail(String to, String firstName, String tempPW)
    {
        String subject  = "Confirmation for H-E-B Recruiting Signup";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(firstName + "," + CARRIAGE_RETURN);
        strBuilder.append(SIGN_UP + CARRIAGE_RETURN);
        strBuilder.append("Your temporary password is " + tempPW + CARRIAGE_RETURN);
        buildSignature(strBuilder);
        String body     = strBuilder.toString();
        sendEmail(to, subject, body);

    }

    public void sendApplicationEmail(String to, String firstName, Application app)
    {
        String subject              = buildApplicationSubject(app);
        StringBuilder strBuilder    = new StringBuilder();
        strBuilder.append(firstName + "," + CARRIAGE_RETURN);
        strBuilder.append("Thank you for expressing interest in working at H-E-B. We have received your application for the following position: " 
                            + app.getRequisition().getJob().getTitle() + ". ");
        strBuilder.append("Please visit <a href=" + HOME + ">H-E-B Recruiting</a> to check your application status and view your profile information." + CARRIAGE_RETURN);
        buildSignature(strBuilder);
        String body     = strBuilder.toString();
        sendEmail(to, subject, body);
    }

    public void sendSubmitGitLinkEmail(String to, String firstName, Application app)
    {
        String subject              = buildApplicationSubject(app);
        StringBuilder strBuilder    = new StringBuilder();
        strBuilder.append(firstName + "," + CARRIAGE_RETURN);
        strBuilder.append(SUBMIT_GIT_LINK + CARRIAGE_RETURN);
        buildSignature(strBuilder);
        String body     = strBuilder.toString();
        sendEmail(to, subject, body);
    }

    public void sendInterviewTimeEmail(String to, String firstName, Application app, LocalDateTime time)
            
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeUtility.simpleScheduleTimeFormat);
        String subject              = buildApplicationSubject(app);
        StringBuilder strBuilder    = new StringBuilder();
        strBuilder.append(firstName + "," + CARRIAGE_RETURN);
        strBuilder.append("You have been scheduled for an interview on " + formatter.format(time) + ". ");
        strBuilder.append("Log into  <a href=" + HOME + ">H-E-B Recruiting</a> to view." + CARRIAGE_RETURN);
        buildSignature(strBuilder);
        String body     = strBuilder.toString();
        sendEmail(to, subject, body);
    }


    private String buildApplicationSubject(Application app)
    {
        String subject  = app.getRequisition().getJob().getTitle() + "-" + app.getRequisition().getRequisitionID() + " at H-E-B";
        return subject;
    }

    private void buildSignature(StringBuilder body)
    {
        body.append("Best regards," + CARRIAGE_RETURN);
        body.append("H-E-B Recruiting" + CARRIAGE_RETURN);
    }
}