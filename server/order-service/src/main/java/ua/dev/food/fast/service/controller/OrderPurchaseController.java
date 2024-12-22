package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.ProductPurchaseDto;
import ua.dev.food.fast.service.domain.dto.request.OrderPurchaseRequest;
import ua.dev.food.fast.service.domain.dto.request.PurchaseItemRequest;
import ua.dev.food.fast.service.domain.dto.response.OrderPurchaseResponse;
import ua.dev.food.fast.service.service.OrderPurchaseService;

@RestController
@RequestMapping("/api/v2/order-purchase")
@RequiredArgsConstructor
public class OrderPurchaseController {
    private final OrderPurchaseService orderPurchaseServiceImpl;

    @PostMapping("/add-order-with-purchase-guest")
    public Mono<ResponseEntity<String>> addOrderWithPurchaseGuest(@RequestBody OrderPurchaseRequest orderPurchaseRequest) {
        return orderPurchaseServiceImpl.addOrderWithPurchaseGuest(orderPurchaseRequest)
            .then(Mono.fromCallable(() -> ResponseEntity.ok("Order was add successfully")));
    }

    @GetMapping("/get-purchases")
    public Mono<ResponseEntity<Flux<ProductPurchaseDto>>> getGuestPurchases(@RequestParam Long orderGuestId) {
        return Mono.just(ResponseEntity.ok().body(orderPurchaseServiceImpl.getGuestPurchases(orderGuestId)));
    }

    @GetMapping("/get-all-purchases") // add auth jwt token
    public Mono<ResponseEntity<Flux<OrderPurchaseResponse>>> getAllOrder() {
        return Mono.just(ResponseEntity.ok().body(orderPurchaseServiceImpl.getAllOrder()));
    }
}
