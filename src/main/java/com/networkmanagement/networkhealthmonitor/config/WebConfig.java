package com.networkmanagement.networkhealthmonitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")   // allow all endpoints
                .allowedOrigins(
                        "http://localhost:4200",
                        "http://127.0.0.1:65217",
                        "http://localhost:4201",
                        "http://127.0.0.1:4200",
                        "http://127.0.0.1:4201",
                        "http://127.0.0.1:62141"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
