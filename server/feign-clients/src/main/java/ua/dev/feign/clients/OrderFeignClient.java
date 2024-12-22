package ua.dev.feign.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient("order-service")
public interface OrderFeignClient {
    @GetMapping("/api/v2/order-purchase/get-purchases")
    Mono<ResponseEntity<Flux<ProductPurchaseDto>>> getGuestPurchases(@RequestParam Long orderGuestId);
}
