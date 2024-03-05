package com.engagewmep.querystudentdata.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // This allows all endpoints. You can restrict paths if needed.
                .allowedOrigins("http://localhost:3000") // Allows only your React app origin. Adjust if your React app runs on a different port.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Adjust allowed methods as necessary.
                .allowCredentials(true);
    }
}
