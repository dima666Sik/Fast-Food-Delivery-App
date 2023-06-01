package dev.food.fast.server.mail.controllers;

import dev.food.fast.server.mail.dto.request.EmailRequest;
import dev.food.fast.server.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
