package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.OrderInfoRequestDto;
import ua.dev.food.fast.service.domain.dto.request.EmailRequest;
import ua.dev.food.fast.service.service.EmailServiceImpl;

/**
 * Controller to handle email-related operations via REST API.
 * Uses RabbitMQ for message queuing and logging for event tracking.
 */
@RestController
@RequestMapping("/api/v2/email")
@RequiredArgsConstructor
@Log4j2
public class EmailController {
    private final RabbitTemplate rabbitTemplate;

    /**
     * Endpoint to send a review email to the support queue.
     *
     * @param emailMessage the email request payload
     * @return a {@link Mono} emitting a {@link ResponseEntity} indicating success or failure
     */
    @PostMapping("/contact-review")
    public Mono<ResponseEntity<String>> sendReviewEmail(@RequestBody EmailRequest emailMessage) {
        return Mono.fromRunnable(() -> rabbitTemplate.convertAndSend("support-email-queue", emailMessage))
            .doOnSuccess(unused -> log.info("Message sent to the queue: {}", emailMessage))
            .thenReturn(ResponseEntity.ok("Email sent to support queue successfully"))
            .onErrorResume(e -> {
                log.error("Failed to send message to queue", e);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send message to queue"));
            });
    }

}
