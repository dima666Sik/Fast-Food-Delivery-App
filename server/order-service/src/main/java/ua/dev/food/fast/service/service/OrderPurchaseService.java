package ua.dev.food.fast.service.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.ProductPurchaseDto;
import ua.dev.food.fast.service.domain.dto.request.OrderPurchaseRequest;
import ua.dev.food.fast.service.domain.dto.response.OrderPurchaseResponse;

import java.util.List;

public interface OrderPurchaseService {
    Mono<Void> addOrderWithPurchaseGuest(@RequestBody OrderPurchaseRequest orderPurchaseRequest);

    Mono<Void> addOrderWithPurchaseUser(String authHeader, OrderPurchaseRequest orderPurchaseRequest);

    Flux<ProductPurchaseDto> getGuestPurchases(@RequestParam Long orderGuestId);

    Flux<OrderPurchaseResponse> getAllOrder();

    Mono<Void> orderAcceptance(String authHeader, List<Long> orderIds, String status);

    Flux<OrderPurchaseResponse> getOrdersWithPurchaseUser(String authHeader);

    Flux<OrderPurchaseResponse> getOrdersCourierProcesses(String authHeader);
}
