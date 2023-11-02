package com.tpi.bda.microservicioalquileres.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TARIFAS")
public class Tarifa {
    @Id
    @GeneratedValue(generator = "TARIFAS")
    @TableGenerator(name = "TARIFAS", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue = "id",
            initialValue = 1, allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "TIPO_TARIFA")
    private int tipoTarifa;

    @Column(name = "DEFINICION")
    private char definicion;

    @Column(name = "DIA_SEMANA")
    private Integer diaSemana;

    @Column(name = "DIA_MES")
    private Integer diaMes;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "MONTO_FIJO_ALQUILER")
    private double montoFijoAlquiler;

    @Column(name = "MONTO_MINUTO_FRACCION")
    private double montoMinutoFraccion;

    @Column(name = "MONTO_KM")
    private double montokm;

    @Column(name = "MONTO_HORA")
    private double montoHora;
}
