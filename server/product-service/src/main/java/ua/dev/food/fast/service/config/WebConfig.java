package ua.dev.food.fast.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    /**
     * The part of the URL that points to the directory containing product images.
     * This value is obtained from the application's properties file using the {@link Value} annotation.
     */
    @Value("${back-end.urls.url-all-images}")
    private String partURLtoImages;

    /**
     * The value obtained from the application's properties file.
     * This value is used to define a URL path pattern in the application.
     */
    @Value("${back-end.urls.url-path-pattern}")
    private String urlPathPattern;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(urlPathPattern)
            .addResourceLocations(Paths.get(System.getProperty("user.dir"), partURLtoImages).toUri().toString())
            .setCacheControl(CacheControl.noCache());
    }
}