package dev.food.fast.server.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${server.part.url.to.images}")
    private String partURLtoImages;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/images/products/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + partURLtoImages + "/")
                .setCacheControl(CacheControl.noCache());
    }
}
