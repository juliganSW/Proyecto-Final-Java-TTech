package com.goodstone.relojeria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Permite solicitudes desde cualquier origen.
                // Cuando conectemos el front, podemos restringirlo a "http://localhost:5500" por ejemplo.
                .allowedOrigins("*")
                // Metodos HTTP permitidos: los mismos que usa nuestra API.
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // Permite cualquier header en las solicitudes.
                .allowedHeaders("*");
    }
}