package backend.Utility;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceService {

    private String signupLink               = "http://34.73.221.154:5000/";
    private String signupSuccessfulText     = "Thank you for expressing interest in working at H-E-B. We have received your application for the following position: <Job Name>. Please visit <a href=" 
                                    + signupLink + ">H-E-B Recruiting</a> to check your application status and view your profile information.";

    @Autowired
    private JavaMailSender sender;

    private String from = "sadctrinity@gmail.com";

    private EmailServiceService() {}

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
    }

    public void sendEmail(String to, String subject, String text) throws AddressException {
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

    public void sendSignUpEmail(String to, String firstName, String tempPW) throws AddressException
    {
        String subject  = "Confirmation for H-E-B Recruiting Signup";
        String body     = buildSignupEmail(firstName, tempPW);
        sendEmail(to, subject, body);

    }

    public String buildSignupEmail(String firstName, String tempPW)
    {
         StringBuilder strBuilder = new StringBuilder();
         strBuilder.append(firstName + ",<br>");
         strBuilder.append(signupSuccessfulText + "<br>");
         strBuilder.append("Your temporary password is " + tempPW + "<br>");
         buildSignature(strBuilder);
         return strBuilder.toString();
    }

    private void buildSignature(StringBuilder body)
    {
        body.append("Best regards,<br>");
        body.append("H-E-B Recruiting<br>");
    }
}