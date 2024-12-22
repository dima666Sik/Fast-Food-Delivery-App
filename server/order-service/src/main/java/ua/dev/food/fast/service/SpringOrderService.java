package ua.dev.food.fast.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication(scanBasePackages = {"ua.dev.feign.clients", "ua.dev.food.fast.service"})
@EnableReactiveFeignClients(
    basePackages = "ua.dev.feign.clients"
)
public class SpringOrderService {
    public static void main(String[] args) {
        SpringApplication.run(SpringOrderService.class, args);
    }
}