package com.tpi.bda.microservicioalquileres.service;

import com.tpi.bda.microservicioalquileres.dto.AlquilerDto;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

public interface IAlquilerService {
    List<Alquiler> findAll();

    Alquiler iniciarAlquiler(long idEstacion, String idCliente) throws ResourceAccessException;

    AlquilerDto finalizarAlquiler(long idAlquiler, long idEstacion, String moneda);
    AlquilerDto mostrarAlquilerFinalizado(Alquiler alquiler, String moneda);

    List<Alquiler> obtenerAlquileresPorMontos(double montoMin, double montoMax);
    Alquiler findById(long idAlquiler);

}
