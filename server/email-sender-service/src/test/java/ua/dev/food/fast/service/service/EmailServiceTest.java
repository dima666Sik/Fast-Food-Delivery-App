package ua.dev.food.fast.service.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.feign.clients.OrderFeignClient;
import ua.dev.feign.clients.OrderInfoRequestDto;
import ua.dev.feign.clients.ProductPurchaseDto;
import ua.dev.food.fast.service.domain.dto.request.EmailRequest;
import ua.dev.food.fast.service.exception_handler.exception.MessageSendingException;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private OrderFeignClient orderFeignClient;

    @InjectMocks
    private EmailServiceImpl emailService;


    @BeforeEach
    void setUp() {
        emailService = new EmailServiceImpl(mailSender, templateEngine, orderFeignClient);
    }

    @Test
    void sendReviewEmailShouldThrowExceptionForInvalidRequest() {
        // Arrange
        EmailRequest invalidEmailRequest = new EmailRequest(null, null, null);

        // Act
        Mono<Void> result = emailService.sendReviewEmail(invalidEmailRequest);

        // Assert
        StepVerifier.create(result)
            .expectError(NoSuchElementException.class)
            .verify();
    }

    @Test
    void sendOrderOnEmailGuestShouldHandleEmptyPurchaseList() {
        // Arrange
        OrderInfoRequestDto orderInfoRequestDto = new OrderInfoRequestDto(
            "customer@example.com", "John", 1L, "123456789", "2024-11-25", "12:00", new BigDecimal("50.00"), true
        );

        when(orderFeignClient.getGuestPurchases(1L))
            .thenReturn(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Flux.empty())));

        // Act
        Mono<Void> result = emailService.sendOrderOnEmailGuest(orderInfoRequestDto);

        // Assert
        StepVerifier.create(result)
            .expectError(MessageSendingException.class)
            .verify();

        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}