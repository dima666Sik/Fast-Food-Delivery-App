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
    private final JavaMailSender mailSender;

    @Value("${spring.mail.email}")
    private String email;

    public ResponseEntity<String> sendReviewEmail(String from, String username, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Fast Food Review");

            String htmlContent = "<html>" +
                    "<head><style>" +
                    "    body { font-family: Arial, sans-serif; font-size: 16px; color: #333; }" +
                    "    h1 { color: orangered; }" +
                    "    table { border-collapse: collapse; }" +
                    "    th, td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #ddd; }" +
                    "    th { background-color: #f2f2f2; font-weight: bold; }" +
                    "    td { font-style: italic; }" +
                    "    td.email { font-weight: bold; color: orangered; }" +
                    "    td.username { font-weight: bold; }" +
                    "</style></head>" +
                    "<body>" +
                    "  <section>" +
                    "    <h1>Hello!</h1>" +
                    "    <p>This email was sent by a client. Please read any comments or suggestions! :)</p>" +
                    "    <div class='table-container'>" +
                    "      <table>" +
                    "        <tr>" +
                    "          <th>Email content:</th>" +
                    "          <td>" + body + "</td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "          <th>Email sender:</th>" +
                    "          <td class='email'>" + from + "</td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "          <th>Best regards:</th>" +
                    "          <td class='username'>" + username + "</td>" +
                    "        </tr>" +
                    "      </table>" +
                    "    </div>" +
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
