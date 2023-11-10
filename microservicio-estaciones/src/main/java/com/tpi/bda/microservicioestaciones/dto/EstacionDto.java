package com.tpi.bda.microservicioestaciones.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstacionDto {
    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String nombre;
    @Max(90)
    @Min(-90)
    private double latitud;
    @Max(180)
    @Min(-180)
    private double longitud;
}
