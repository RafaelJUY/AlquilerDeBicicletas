package com.tpi.bda.microservicioestaciones.service.convert;

import com.tpi.bda.microservicioestaciones.dto.EstacionDto;
import com.tpi.bda.microservicioestaciones.model.entity.Estacion;

public class EstacionEntityDto {
    public static Estacion estacionDtoAEntidad(EstacionDto dto){
        Estacion estacion = new Estacion();
        estacion.setNombre(dto.getNombre());
        estacion.setLatitud(dto.getLatitud());
        estacion.setLongitud(dto.getLongitud());

        return estacion;
    }
}
