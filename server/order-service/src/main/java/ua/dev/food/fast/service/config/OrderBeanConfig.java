package ua.dev.food.fast.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.dev.jwt.service.JwtDecodeService;

@Configuration
public class OrderBeanConfig {
    @Bean
    public JwtDecodeService jwtDecodeService() {
        return new JwtDecodeService();
    }
}
