package com.tpi.bda.microservicioalquileres.service;

import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.List;

public interface IAlquilerService {
    List<Alquiler> findAll();

    Alquiler iniciarAlquiler(long idEstacion, long idCliente) throws ResourceAccessException;

    Alquiler finalizarAlquiler(long idAlquiler, long idEstacion);

    List<Alquiler> obtenerAlquileresPorMontos(double montoMin, double montoMax);

}
