package com.tpi.bda.microservicioalquileres.service.impl;

import com.tpi.bda.microservicioalquileres.model.Estacion;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import com.tpi.bda.microservicioalquileres.repository.IAlquilerRepository;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import com.tpi.bda.microservicioalquileres.service.ITarifaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public Alquiler iniciarAlquiler(long idEstacion, long idCliente) throws NoSuchElementException {

        Estacion e = this.buscarEstacion(idEstacion);
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Alquiler a = new Alquiler();
        a.setIdCliente(String.valueOf(idCliente));
        a.setEstacionRetiro(e);
        a.setFechaHoraRetiro(fechaHoraActual);
        a.setEstado(1);
        return this.alquilerRepository.save(a);
    }

    public Alquiler finalizarAlquiler(long idAlquiler, long idEstacion){

        Alquiler alquiler = alquilerRepository.findById(idAlquiler).orElseThrow();
        Tarifa tarifa = tarifaService.getTarifaDeHoy();
        Estacion estacion = buscarEstacion(idEstacion);

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

        return null;

    }

    public Estacion buscarEstacion(Long idEstacion) {
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

    public double buscarDistanciaEntreEstaciones(Long idEstacionRetiro, Long idEstacionDevolucion) {
        // Creación de una instancia de RestTemplate
        try {
            // Creación de la instancia de RequestTemplate
            RestTemplate template = new RestTemplate();
            // Creación de la entidad a enviar
/*            ResponseEntity<Estacion> res = template.getForEntity(
                    "http://localhost:8001/api/estaciones/distanciaEntreEstaciones{idEstacion}", Estacion.class, idEstacion
            );*/

            ResponseEntity<Estacion> res = template.exchange(
                    "http://localhost:8001/api/estaciones?estacion1={estacion1}&estacion2={estacion2}",
                    HttpMethod.GET,
                    entity,
                    Estacion.class, estacion1, estacion2
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
