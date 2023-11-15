package com.tpi.bda.microservicioalquileres.servicioRemoto;

import com.tpi.bda.microservicioalquileres.model.entity.Estacion;

public interface ServicioRemotoEstacion {
    Estacion buscarEstacion(Long idEstacion);
    Double obtenerDistanciaAEstacionDevolucion(Long idEstacionRetiro, Long idEstacionDevolucion);
}
