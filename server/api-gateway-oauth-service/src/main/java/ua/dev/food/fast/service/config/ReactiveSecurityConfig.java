package ua.dev.food.fast.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import ua.dev.food.fast.service.config.jwt.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class ReactiveSecurityConfig {

    private final CorsConfigurationSource corsConfig;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ServerLogoutHandler logoutHandler;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
            .cors(cors -> cors.configurationSource(corsConfig))  // Configure CORS if needed
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/api/v2/auth/**", "/api/v2/slider/**",
                    "/public/**", "/api/v2/foods/**",
                    "/api/v2/email/**", "/api/v2/order-purchase/**")
                .permitAll()
                .anyExchange()
                .authenticated()
            )
            .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)  // Adding JWT auth filter at the authentication phase
            .logout(logoutSpec -> logoutSpec
                .logoutUrl("/api/v2/auth/logout")
                .logoutHandler(logoutHandler)
                .logoutSuccessHandler((exchange, authentication) -> {
                    SecurityContextHolder.clearContext();
                    return exchange.getExchange().getResponse().setComplete();
                })
            )
            .build();
    }

}
