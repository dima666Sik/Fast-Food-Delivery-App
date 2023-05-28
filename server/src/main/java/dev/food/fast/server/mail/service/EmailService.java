package dev.food.fast.server.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.email}")
    private String email;

    public ResponseEntity<String> sendReviewEmail(String from, String username, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Fast Food Review");

            // HTML-code mail message body
            String htmlContent = "<html>" +
                    "<head><style>" +
                    "    body { font-family: Arial, sans-serif; font-size: 16px; }" +
                    "    h1 { color: orangered; }" +
                    "</style></head>" +
                    "<body>" +
                    "  <section>" +
                    "    <h1>Hello!</h1>" +
                    "    <p>This email was sent by a client, please read any comments or suggestions! :)</p>" +
                    "    <p>Email content: <strong><i>" + body + "</i></strong></p>" +
                    "    <p>Email sender: <strong>" + from + "</strong></p>" +
                    "    <p>Best regards, <i><b>" + username + "</b></i></p>" +
                    "  </section>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }
    }

}
