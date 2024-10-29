package ua.dev.food.fast.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.http.CacheControl;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for configuring the web resources in the application.
 * It extends the {@link WebFluxConfigurer} interface to override the default resource handling behavior.
 */
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

    /**
     * Overrides the default resource handling behavior to serve product images from a specific directory.
     *
     * @param registry The {@link ResourceHandlerRegistry} used to register resource handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(urlPathPattern)
            .addResourceLocations(Paths.get(System.getProperty("user.dir"), partURLtoImages).toUri().toString())
            .setCacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePrivate());
    }
}

