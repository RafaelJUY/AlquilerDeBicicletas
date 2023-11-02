package com.tpi.bda.microservicioestaciones.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ESTACIONES")
public class Estacion {
    @Id
    @GeneratedValue(generator = "ESTACIONES")
    @TableGenerator(name = "ESTACIONES", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue = "id",
            initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FECHA_HORA_CREACION")
    private LocalDateTime fechaHoraCreacion;

    @Column(name = "LATITUD")
    private double latitud;

    @Column(name = "LONGITUD")
    private double longitud;
}