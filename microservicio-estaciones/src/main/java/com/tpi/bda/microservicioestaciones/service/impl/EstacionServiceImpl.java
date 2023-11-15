package com.tpi.bda.microservicioestaciones.service.impl;

import com.tpi.bda.microservicioestaciones.exception.personalized.EntidadNoExistenteException;
import com.tpi.bda.microservicioestaciones.exception.personalized.SinRegistrosDisponiblesException;
import com.tpi.bda.microservicioestaciones.model.Ubicacion;
import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import com.tpi.bda.microservicioestaciones.repository.IEstacionRepository;
import com.tpi.bda.microservicioestaciones.service.IEstacionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstacionServiceImpl implements IEstacionService {
    private final IEstacionRepository estacionRepository;

    public EstacionServiceImpl(IEstacionRepository estacionRepository) {
        this.estacionRepository = estacionRepository;
    }

    @Override
    public List<Estacion> findAll() {
        return this.estacionRepository.findAll();
    }

    @Override
    public Estacion findEstacionCercana(Ubicacion ubicacion) {
        List<Estacion> estaciones = this.findAll();

        if (estaciones.isEmpty()){
            throw new SinRegistrosDisponiblesException("No hay estaciones cargadas en Base de Datos");
        }

        Estacion estacionCercana = estaciones.get(0);
        double menorDistancia = calularDistancia(ubicacion, estacionCercana);

        double distancia;
        for (Estacion estacion : estaciones) {
            distancia = calularDistancia(ubicacion, estacion);
            if (menorDistancia > distancia) {
                menorDistancia = distancia;
                estacionCercana = estacion;
            }
        }
        return estacionCercana;
    }

    @Override
    public double calularDistancia(Estacion estacion1, Estacion estacion2){
        double distancia = 110000 * Math.sqrt(Math.pow(estacion1.getLatitud() - estacion2.getLatitud(), 2) +
                Math.pow(estacion1.getLongitud() - estacion2.getLongitud(),2));

        return distancia;
    }

    @Override
    public double calularDistancia(Ubicacion ubicacion, Estacion estacion){
        double distancia = 110000 * Math.sqrt(Math.pow(ubicacion.getLatitud() - estacion.getLatitud(), 2) +
                Math.pow(ubicacion.getLongitud() - estacion.getLongitud(),2));

        return distancia;
    }
    @Override
    public Estacion findEstacionById(long idEstacion) {
        return estacionRepository
                .findById(idEstacion)
                .orElseThrow(() -> new EntidadNoExistenteException("No existe la estación. " +
                        "\nEstación id: " + idEstacion ));
    }

    @Override
    public Estacion crearEstacion(Estacion estacion) {
        estacion.setId(estacionRepository.getMaxId() + 1);
        estacion.setFechaHoraCreacion(LocalDateTime.now());
        return estacionRepository.save(estacion);
    }
}
