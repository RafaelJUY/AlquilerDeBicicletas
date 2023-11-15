package com.tpi.bda.microservicioalquileres.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaConversionDto {
    private String moneda;
    private double importe;
}
