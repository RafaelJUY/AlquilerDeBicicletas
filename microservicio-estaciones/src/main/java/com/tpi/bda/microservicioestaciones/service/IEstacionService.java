package com.tpi.bda.microservicioestaciones.service;

import com.tpi.bda.microservicioestaciones.model.Ubicacion;
import com.tpi.bda.microservicioestaciones.model.entity.Estacion;

import java.util.List;

public interface IEstacionService {
    List<Estacion> findAll();

//    Estacion findEstacionCercana(double latitud, double longitud);
    Estacion findEstacionCercana(Ubicacion ubicacion);
    Estacion findEstacionById(long idEstacion);
    
//    double calularDistanciaEstaciones(long idEstacion1, long idEstacion2);
    double calularDistancia(Estacion estacion1, Estacion estacion2);
    double calularDistancia(Ubicacion ubicacion, Estacion estacion);

    Estacion crearEstacion(Estacion estacion);
}
