package ua.dev.food.fast.service.service;

import reactor.core.publisher.Mono;
import ua.dev.feign.clients.OrderInfoRequestDto;
import ua.dev.food.fast.service.domain.dto.request.EmailRequest;

public interface EmailService {
    Mono<Void> sendReviewEmail(EmailRequest emailMessage);

    Mono<Void> sendOrderOnEmailGuest(OrderInfoRequestDto orderInfoRequestDto);
}
