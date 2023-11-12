package com.tpi.bda.microservicioalquileres.service;

import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;

import java.util.List;

public interface ITarifaService {
    List<Tarifa> getAll();

    /*List<Tarifa> findByDefinicion(char definicion);*/
    Tarifa getTarifaDeHoy();
}
