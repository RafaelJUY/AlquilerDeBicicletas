package com.tpi.bda.microservicioalquileres.service.impl;

import com.tpi.bda.microservicioalquileres.dto.AlquilerDto;
import com.tpi.bda.microservicioalquileres.dto.ConversionDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;
import com.tpi.bda.microservicioalquileres.exception.personalized.DatosInconsistentesException;
import com.tpi.bda.microservicioalquileres.exception.personalized.EntidadNoExistenteException;
import com.tpi.bda.microservicioalquileres.exception.personalized.SinRegistrosDisponiblesException;
import com.tpi.bda.microservicioalquileres.model.EstadoAlquiler;
import com.tpi.bda.microservicioalquileres.model.TipoMoneda;
import com.tpi.bda.microservicioalquileres.model.entity.Estacion;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import com.tpi.bda.microservicioalquileres.repository.IAlquilerRepository;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import com.tpi.bda.microservicioalquileres.service.ITarifaService;
import com.tpi.bda.microservicioalquileres.servicioRemoto.ServicioRemotoEstacion;
import com.tpi.bda.microservicioalquileres.servicioRemoto.ServicioRemotoMoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.zip.DataFormatException;

@Service
public class AlquilerServiceImpl implements IAlquilerService {
    @Autowired
    ServicioRemotoEstacion servicioRemotoEstacion;

    @Autowired
    ServicioRemotoMoneda servicioRemotoMoneda;
    private final IAlquilerRepository alquilerRepository;
    private final ITarifaService tarifaService;

    public AlquilerServiceImpl(IAlquilerRepository alquilerRepository,ITarifaService tarifaService) {
        this.alquilerRepository = alquilerRepository;
        this.tarifaService = tarifaService;
    }

    @Override
    public List<Alquiler> findAll() {
        return alquilerRepository.findAll();
    }

    @Override
    public Alquiler iniciarAlquiler(long idEstacion, String idCliente) throws NoSuchElementException, ResourceAccessException {
        Estacion estacion = servicioRemotoEstacion.buscarEstacion(idEstacion);

        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Alquiler alquiler = new Alquiler();

        alquiler.setIdCliente(idCliente);
        alquiler.setEstacionRetiro(estacion);
        alquiler.setFechaHoraRetiro(fechaHoraActual);
        alquiler.setEstado(EstadoAlquiler.INICIADO.getId());
        return this.alquilerRepository.save(alquiler);
    }

    @Override
    public AlquilerDto finalizarAlquiler(long idAlquiler, long idEstacionDevolucion, String moneda){
        Alquiler alquiler = this.findById(idAlquiler);

        if (alquiler.getEstado() == EstadoAlquiler.FINALIZADO.getId()){
            throw new DatosInconsistentesException("El alquiler ya fue finalizado previamente " +
                    "Alquiler id: " + idAlquiler);
        }
        if (alquiler.getEstacionRetiro().getId() == idEstacionDevolucion) {
            throw new DatosInconsistentesException("La estacion de devolucion no puede ser igual a la de retiro");
        }

        Estacion estacion = servicioRemotoEstacion.buscarEstacion(idEstacionDevolucion);

        Tarifa tarifa = tarifaService.getTarifaDeHoy();

        alquiler.setTarifa(tarifa);
        alquiler.setEstacionDevolucion(estacion);
        alquiler.setEstado(EstadoAlquiler.FINALIZADO.getId());
        alquiler.setFechaHoraDevolucion(LocalDateTime.now());
        alquiler.setMonto(calcularMonto(alquiler, tarifa));

        alquiler = alquilerRepository.save(alquiler);
        return mostrarAlquilerFinalizado(alquiler, moneda);
    }

    @Override
    public AlquilerDto mostrarAlquilerFinalizado(Alquiler alquiler, String moneda){
        if (!this.validarMoneda(moneda)){
            moneda = "ARS";
        }

        // creacion del dto
        AlquilerDto response = new AlquilerDto();
        response.setEstacionRetiro(alquiler.getEstacionRetiro().getNombre());
        response.setEstacionDevolucion(alquiler.getEstacionDevolucion().getNombre());
        response.setFechaHoraRetiro(alquiler.getFechaHoraRetiro());
        response.setFechaHoraDevolucion(alquiler.getFechaHoraDevolucion());

        if (moneda.equals("ARS")) {
            response.setMoneda(moneda);
            response.setMonto(alquiler.getMonto());
        } else {
            ConversionDto conversion = new ConversionDto(moneda, alquiler.getMonto());

            Optional<RespuestaConversionDto> respuestaOp = servicioRemotoMoneda.obtenerConversion(conversion);

            if (respuestaOp.isPresent()){
                RespuestaConversionDto obtenerConversion = respuestaOp.get();
                response.setMoneda(obtenerConversion.getMoneda());
                response.setMonto(obtenerConversion.getImporte());
            }else {
                moneda = "ARS";
                response.setMoneda(moneda);
                response.setMonto(alquiler.getMonto());
            }
        }

        return response;
    }

    private boolean validarMoneda(String moneda){
        boolean valida = false;

        for (TipoMoneda tipo : TipoMoneda.values()) {
            if (tipo.name().equals(moneda)) {
                valida = true;
                break;
            }
        }
        return valida;
    }

    private double calcularMonto(Alquiler alquiler, Tarifa tarifa){
        double monto = tarifa.getMontoFijoAlquiler();

        long minutosTotales = alquiler.getFechaHoraRetiro().until( alquiler.getFechaHoraDevolucion(), ChronoUnit.MINUTES );

        long horas = minutosTotales / 60;

        // Sumar el monto por la cantidad de minutos
        if ((minutosTotales % 60) < 31) { // si los minutos que no completan una hora son < 31 min
            monto += tarifa.getMontoMinutoFraccion() * (minutosTotales % 60);
        }
        else { // si son mas de 31 min se cuenta como hora completa
            horas += 1;
        }

        // Sumar el monto por la cantidad de horas
        monto += tarifa.getMontoHora() * horas;

        // traer la distancia entre estaciones
        double distancia = servicioRemotoEstacion.obtenerDistanciaAEstacionDevolucion(alquiler.getEstacionRetiro().getId(),
                alquiler.getEstacionDevolucion().getId());
        long distanciaEnKm = (long) distancia / 1000;

        monto += distanciaEnKm * tarifa.getMontokm();

        return monto;
    }

    @Override
    public Alquiler findById(long idAlquiler) {
        return alquilerRepository
                .findById(idAlquiler)
                .orElseThrow(() -> new EntidadNoExistenteException("No existe el alquiler. \nAlquiler id: " + idAlquiler ));
    }

    public List<Alquiler> obtenerAlquileresPorMontos(double montoMin, double montoMax) {
        List<Alquiler> respuesta = new ArrayList<>();

        // Validar monto minimo y maximo
        if (montoMax < montoMin){
            return respuesta;
        }

        respuesta = alquilerRepository.findByMontoBetween(montoMin, montoMax);
        return respuesta;

    }



}
