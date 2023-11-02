package com.tpi.bda.microservicioestaciones.service;

import com.tpi.bda.microservicioestaciones.model.entity.Estacion;

import java.util.List;

public interface IEstacionService {
    List<Estacion> findAll();

    Estacion findEstacionCercana(double latitud, double longitud);
}