package com.tpi.bda.microservicioalquileres.model;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Estacion {
    @EqualsAndHashCode.Include
    private Long id;
    private String nombre;
    private LocalDateTime fechaHoraCreacion;
    private double latitud;
    private double longitud;
}
