package com.tpi.bda.microservicioalquileres.service.impl;

import com.tpi.bda.microservicioalquileres.dto.AlquilerDto;
import com.tpi.bda.microservicioalquileres.dto.ConversionDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;
import com.tpi.bda.microservicioalquileres.model.entity.Estacion;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import com.tpi.bda.microservicioalquileres.repository.IAlquilerRepository;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import com.tpi.bda.microservicioalquileres.service.ITarifaService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AlquilerServiceImpl implements IAlquilerService {
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

        Estacion e = this.buscarEstacion(idEstacion);
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Alquiler a = new Alquiler();
        a.setIdCliente(idCliente);
        a.setEstacionRetiro(e);
        a.setFechaHoraRetiro(fechaHoraActual);
        a.setEstado(1);
        return this.alquilerRepository.save(a);
    }

    public AlquilerDto finalizarAlquiler(long idAlquiler, long idEstacion, String moneda){

        Alquiler alquiler = alquilerRepository.findById(idAlquiler).orElseThrow();
        Tarifa tarifa = tarifaService.getTarifaDeHoy();
        Estacion estacion = buscarEstacion(idEstacion);

        if (alquiler.getEstacionDevolucion() != null) {
            return null;
        }

        alquiler.setTarifa(tarifa);
        alquiler.setEstacionDevolucion(estacion);
        alquiler.setEstado(2);
        alquiler.setFechaHoraDevolucion(LocalDateTime.now());

        // Calcular monto
        double monto = 0;
        // Sumar el monto fijo
        monto += tarifa.getMontoFijoAlquiler();
        long minutes = alquiler.getFechaHoraRetiro().until( alquiler.getFechaHoraDevolucion(), ChronoUnit.MINUTES );

        long horas = minutes / 60;

        // Sumar el monto por la cantidad de minutos
        if ((minutes % 60) < 31) { // si los minutos que no completan una hora son < 31 min
            monto += tarifa.getMontoMinutoFraccion() * (minutes % 60);
        }
        else { // si son mas de 31 min se cuenta como hora completa
            horas += 1;
        }

        // Sumar el monto por la cantidad de horas
        monto += tarifa.getMontoHora() * horas;

        // traer la distancia entre estaciones
        double distancia = buscarDistanciaEntreEstaciones(alquiler.getEstacionRetiro().getId(),
                alquiler.getEstacionDevolucion().getId());
        long distanciaEnKm = (long) distancia / 1000;

        monto += distanciaEnKm * tarifa.getMontokm();

        alquiler.setMonto(monto);

        // creacion del dto
        AlquilerDto response = new AlquilerDto();
        response.setEstacionRetiro(alquiler.getEstacionRetiro().getNombre());
        response.setEstacionDevolucion(alquiler.getEstacionDevolucion().getNombre());
        response.setFechaHoraRetiro(alquiler.getFechaHoraRetiro());
        response.setFechaHoraDevolucion(alquiler.getFechaHoraDevolucion());

        if (!moneda.equals("")) {
            ConversionDto conversion = new ConversionDto(moneda, monto);
            RespuestaConversionDto obtenerConversion = this.obtenerConversion(conversion);
            response.setMoneda(obtenerConversion.getMoneda());
            response.setMonto(obtenerConversion.getImporte());
        }
        else {
            response.setMoneda("ARS");
            response.setMonto(alquiler.getMonto());
        }


        alquilerRepository.save(alquiler);
        return response;

    }

    public RespuestaConversionDto obtenerConversion(ConversionDto conversion) {
        try {
            // Creación de la instancia de RequestTemplate
            RestTemplate template = new RestTemplate();

            // Respuesta de la peticion
            ResponseEntity<RespuestaConversionDto> res = template.postForEntity("http://34.82.105.125:8080/convertir", conversion, RespuestaConversionDto.class);

            // Se comprueba si el código de repuesta es de la familia 200
            if (res.getStatusCode().is2xxSuccessful()) {
                //log.debug("Persona creada correctamente: {}", res.getBody());
                return res.getBody();
            } else {
                //log.warn("Respuesta no exitosa: {}", res.getStatusCode());

            }
        } catch (HttpClientErrorException ex) {
            throw new NoSuchElementException();
            // La repuesta no es exitosa.
            //log.error("Error en la petición", ex);
        }
        return null;
    }

    public Estacion buscarEstacion(Long idEstacion) throws ResourceAccessException{
        // Creación de una instancia de RestTemplate
        try {
            // Creación de la instancia de RequestTemplate
            RestTemplate template = new RestTemplate();

            // Creación de la entidad a enviar
            ResponseEntity<Estacion> res = template.getForEntity(
                    "http://localhost:8001/api/estaciones/{idEstacion}", Estacion.class, idEstacion
            );
            // Se comprueba si el código de repuesta es de la familia 200
            if (res.getStatusCode().is2xxSuccessful()) {
                //log.debug("Persona creada correctamente: {}", res.getBody());
                return res.getBody();
            } else {
                //log.warn("Respuesta no exitosa: {}", res.getStatusCode());

            }
        } catch (HttpClientErrorException ex) {
            throw new NoSuchElementException();
            // La repuesta no es exitosa.
            //log.error("Error en la petición", ex);
        }
        return null;

    }

    public Double buscarDistanciaEntreEstaciones(Long idEstacionRetiro, Long idEstacionDevolucion) {
        // Creación de una instancia de RestTemplate
        try {
            // Creación de la instancia de RequestTemplate
            RestTemplate template = new RestTemplate();

            // parametros
            HashMap<String, String> params = new HashMap<>();
            params.put("estacion1", idEstacionRetiro.toString());
            params.put("estacion2", idEstacionDevolucion.toString());

            // Creación de la entidad a enviar
            String url = "http://localhost:8001/api/estaciones/distanciaEntreEstaciones/{estacion1}/{estacion2}";
            ResponseEntity<Double> res = template.getForEntity(
                    url, Double.class, params
            );

            // Se comprueba si el código de repuesta es de la familia 200
            if (res.getStatusCode().is2xxSuccessful()) {
                //log.debug("Persona creada correctamente: {}", res.getBody());
                return res.getBody();
            } else {
                //log.warn("Respuesta no exitosa: {}", res.getStatusCode());

            }
        } catch (HttpClientErrorException ex) {
            throw new NoSuchElementException();
            // La repuesta no es exitosa.
            //log.error("Error en la petición", ex);
        }
        return null;

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
