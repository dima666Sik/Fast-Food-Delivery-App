package ua.dev.feign.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient("product-service")
public interface ProductFeignClient {
    @GetMapping("/api/v2/product/foods/product/{id}")
    Mono<ProductResponseDto> getProductById(@PathVariable("id") Long id);
}
