package com.tpi.bda.microservicioalquileres.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ALQUILERES")
public class Alquiler {
    @Id
    @GeneratedValue(generator = "ALQUILERES")
    @TableGenerator(name = "ALQUILERES", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue="id",
            initialValue=1, allocationSize=1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ID_CLIENTE")
    private String idCliente;

    @Column(name = "ESTADO")
    private Integer estado; //1: Iniciado, 2: Finalizado :)

    @JoinColumn(name = "ESTACION_RETIRO")
    @OneToOne
    private Estacion estacionRetiro;

    @JoinColumn(name = "ESTACION_DEVOLUCION")
    @OneToOne
    private Estacion estacionDevolucion;

    @Column(name = "FECHA_HORA_RETIRO")
    private LocalDateTime fechaHoraRetiro;

    @Column(name = "FECHA_HORA_DEVOLUCION")
    private LocalDateTime fechaHoraDevolucion;

    @Column(name = "MONTO")
    private double monto;

    @JoinColumn(name = "ID_TARIFA")
    @OneToOne
    private Tarifa tarifa;
}
