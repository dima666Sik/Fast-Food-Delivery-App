package ua.dev.feign.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient("gateway-auth-service")
public interface UserFeignClient {
    @GetMapping("/api/v2/auth/users/{id}")
    Mono<UserDto> getUserById(@PathVariable("id") Long id);
}
