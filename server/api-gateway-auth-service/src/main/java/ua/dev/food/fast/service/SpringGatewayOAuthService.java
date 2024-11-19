package ua.dev.food.fast.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients(
    basePackages = "ua.dev.feign.clients"
)
@EnableScheduling
public class SpringGatewayOAuthService {
    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayOAuthService.class, args);
    }
}