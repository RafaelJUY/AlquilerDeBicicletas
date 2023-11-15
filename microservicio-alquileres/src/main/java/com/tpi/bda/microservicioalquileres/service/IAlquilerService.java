package com.tpi.bda.microservicioalquileres.service;

import com.tpi.bda.microservicioalquileres.dto.AlquilerDto;
import com.tpi.bda.microservicioalquileres.dto.ConversionDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.List;

public interface IAlquilerService {
    List<Alquiler> findAll();

    Alquiler iniciarAlquiler(long idEstacion, String idCliente) throws ResourceAccessException;

    /*AlquilerDto finalizarAlquiler(long idAlquiler, long idEstacion, String moneda);*/
    AlquilerDto finalizarAlquiler(long idAlquiler, long idEstacion, String moneda);
    AlquilerDto mostrarAlquilerFinalizado(Alquiler alquiler, String moneda);

    List<Alquiler> obtenerAlquileresPorMontos(double montoMin, double montoMax);

//    RespuestaConversionDto obtenerConversion(ConversionDto conversionDto);

    Alquiler findById(long idAlquiler);

}
