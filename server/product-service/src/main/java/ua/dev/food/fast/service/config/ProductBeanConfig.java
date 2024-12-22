package ua.dev.food.fast.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ua.dev.jwt.service.JwtDecodeService;

@Configuration
public class ProductBeanConfig {
    @Bean
    public JwtDecodeService jwtDecodeService() {
        return new JwtDecodeService();
    }
}
