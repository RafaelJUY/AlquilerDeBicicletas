package com.tpi.bda.microservicioalquileres.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversionDto {
    private String moneda_destino;
    private double importe;
}
