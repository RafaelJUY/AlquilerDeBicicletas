package com.tpi.bda.microservicioalquileres.servicioRemoto;

import com.tpi.bda.microservicioalquileres.dto.ConversionDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;

public interface ServicioRemotoMoneda {
    RespuestaConversionDto obtenerConversion(ConversionDto conversion);
}
