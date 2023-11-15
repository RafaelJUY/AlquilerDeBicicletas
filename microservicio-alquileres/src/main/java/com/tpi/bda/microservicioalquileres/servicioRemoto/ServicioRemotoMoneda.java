package com.tpi.bda.microservicioalquileres.servicioRemoto;

import com.tpi.bda.microservicioalquileres.dto.ConversionDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;

import java.util.Optional;

public interface ServicioRemotoMoneda {
    Optional<RespuestaConversionDto> obtenerConversion(ConversionDto conversion);
}
