package com.tpi.bda.microservicioestaciones.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstacionDto {
    private String nombre;
    private double latitud;
    private double longitud;
}
