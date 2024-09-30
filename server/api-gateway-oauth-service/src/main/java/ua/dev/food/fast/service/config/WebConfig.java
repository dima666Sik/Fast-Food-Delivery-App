package ua.dev.food.fast.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.session.WebSessionManager;
import reactor.core.publisher.Mono;

@Configuration
public class WebConfig {

    /**
     * Configures a custom WebSessionManager to emulate stateless sessions.
     *
     * @return a WebSessionManager that does not create sessions.
     */
    @Bean
    public WebSessionManager webSessionManager() {
        // Return an empty Mono to emulate SessionCreationPolicy.STATELESS
        return exchange -> Mono.empty();
    }

}
