package com.tpi.bda.microservicioestaciones.service.impl;

import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import com.tpi.bda.microservicioestaciones.repository.IEstacionRepository;
import com.tpi.bda.microservicioestaciones.service.IEstacionService;
import org.springframework.stereotype.Service;

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
    public Estacion findEstacionCercana(double latitud, double longitud) {
        List<Estacion> estaciones = this.findAll();
        Estacion estacionCercana = estaciones.get(0);
        double menorDistancia = 110000 * Math.sqrt(Math.pow(estacionCercana.getLatitud() - latitud, 2) +
                Math.pow(estacionCercana.getLongitud() - longitud,2));

        for (Estacion estacion : estaciones) {
            double distancia = 110000 * Math.sqrt(Math.pow(estacion.getLatitud() - latitud, 2) +
                    Math.pow(estacion.getLongitud() - longitud,2));

            if (distancia < menorDistancia) {
                estacionCercana = estacion;
                menorDistancia = distancia;
            }
        }

        return estacionCercana;
    }
    
    public double calularDistanciaEstaciones(long idEstacion1, long idEstacion2){
        Estacion estacion1 = findEstacionById(idEstacion1);
        Estacion estacion2 = findEstacionById(idEstacion2);

        double distancia = 110000 * Math.sqrt(Math.pow(estacion1.getLatitud() - estacion2.getLatitud(), 2) +
                Math.pow(estacion1.getLongitud() - estacion2.getLongitud(),2));
        
        return distancia;
    }

    public Estacion findEstacionById(long idEstacion) {
        return estacionRepository.findById(idEstacion).orElseThrow();
    }
}
