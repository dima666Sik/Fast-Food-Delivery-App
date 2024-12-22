package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ua.dev.food.fast.service.domain.dto.request.EmailRequest;

@Component
@Log4j2
@RequiredArgsConstructor
public class EmailConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "support-email-queue")
    public void processSupportEmail(EmailRequest emailRequest) {
        log.info("Processing support email: {}", emailRequest);

        emailService.sendReviewEmail(emailRequest)
            .doOnSuccess(unused -> log.info("Email sent successfully to support team"))
            .doOnError(e -> log.error("Failed to process support email", e))
            .subscribe();
    }
}
