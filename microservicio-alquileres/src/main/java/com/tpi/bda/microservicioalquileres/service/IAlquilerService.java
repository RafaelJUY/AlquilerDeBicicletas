package com.tpi.bda.microservicioalquileres.service;

import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;

import java.util.List;

public interface IAlquilerService {
    List<Alquiler> findAll();

    Alquiler iniciarAlquiler(long idEstacion);
}
