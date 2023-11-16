package com.tpi.bda.gateway.Gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
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

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange(exchanges -> exchanges

                        // Rutas para ADMINISTRADOR
                        .pathMatchers(HttpMethod.POST, "/api/estaciones").hasRole("ADMINISTRADOR")
                        .pathMatchers(HttpMethod.GET,"/api/alquileres" ).hasRole("ADMINISTRADOR")
                        .pathMatchers(HttpMethod.GET,"/api/alquileres/filtrar" ).hasRole("ADMINISTRADOR")

                        // Rutas para CLIENTE
                        .pathMatchers(HttpMethod.GET,"/api/estaciones").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.POST, "/api/alquileres").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.PATCH, "/api/alquileres").hasRole("CLIENTE")
                        // Otras rutas permitidas para todos los roles autenticados
                        .anyExchange().authenticated()

                ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Se especifica el nombre del claim a analizar
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // Se agrega este prefijo en la conversión por una convención de Spring
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        // Se asocia el conversor de Authorities al Bean que convierte el token JWT a un objeto Authorization
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new ReactiveJwtGrantedAuthoritiesConverterAdapter(grantedAuthoritiesConverter));

        return jwtAuthenticationConverter;
    }
}
