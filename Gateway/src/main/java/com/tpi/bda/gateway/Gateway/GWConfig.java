package com.tpi.bda.gateway.Gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GWConfig {
    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${url-microservicio-estaciones}") String uriEstaciones,
                                        @Value("${url-microservicio-alquileres}") String uriAlquileres) {
        return builder.routes()
                // Ruteo al Microservicio de estaciones
                .route(p -> p.path("/api/estaciones/**").uri(uriEstaciones))
                // Ruteo al Microservicio de alquileres
                .route(p -> p.path("/api/alquileres/**").uri(uriAlquileres))
                .build();

    }
}
