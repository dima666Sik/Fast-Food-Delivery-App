package ua.dev.food.fast.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//
//import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@RequiredArgsConstructor
public class SecurityConfig {
//    private CorsConfigurationSource corsConfigurationSource;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource))
//            .authorizeHttpRequests(requests -> requests
//                    .requestMatchers("/api/v1/auth/**", "/api/v1/slider/**",
//                            "/public/**", "/api/v1/foods/**",
//                            "/api/v1/email/**", "/api/v1/order-purchase/**")
//                    .permitAll()
//                    .anyRequest()
//                    .authenticated()
//            );
//        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.formLogin(withDefaults());
//        http.httpBasic(withDefaults());
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
