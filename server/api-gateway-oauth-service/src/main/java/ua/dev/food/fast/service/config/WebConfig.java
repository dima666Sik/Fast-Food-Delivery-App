package ua.dev.food.fast.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.http.CacheControl;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Value("${back-end.urls.url-all-images}")
    private String partURLtoImages;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/images/products/**")
                .addResourceLocations(Paths.get(System.getProperty("user.dir"), partURLtoImages).toUri().toString())
                .setCacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePrivate());
    }
}

