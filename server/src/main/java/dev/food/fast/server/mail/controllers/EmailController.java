package dev.food.fast.server.mail.controllers;

import dev.food.fast.server.mail.pojo.EmailRequest;
import dev.food.fast.server.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailService service;

    @PostMapping("/contact-review")
    public ResponseEntity<?> sendReviewEmail(@RequestBody EmailRequest emailMessage) {
        return service.sendReviewEmail(emailMessage.getFrom(), emailMessage.getUsername(), emailMessage.getMessage());
    }

}
