package com.tpi.bda.microservicioestaciones.cotroller;

import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import com.tpi.bda.microservicioestaciones.service.IEstacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/estaciones")
public class EstacionController {
    private final IEstacionService estacionService;

    public EstacionController(IEstacionService estacionService) {
        this.estacionService = estacionService;
    }


    @GetMapping
    public ResponseEntity<List<Estacion>> findAll() {
        List<Estacion> values = this.estacionService.findAll();
        return ResponseEntity.ok(values);
    }

    @GetMapping("/masCercana")
    public ResponseEntity<Estacion> findEstacionCercana(@RequestParam("latitud") double latitud,
                                                        @RequestParam("longitud") double longitud) {
        Estacion estacionCercana = this.estacionService.findEstacionCercana(latitud, longitud);
        return ResponseEntity.ok(estacionCercana);

    }

    @GetMapping("/distanciaEntreEstaciones/{estacion1}/{estacion2}")
    public ResponseEntity<Double> calularDistanciaEstaciones(@PathVariable("estacion1") long estacion1,
                                                             @PathVariable("estacion2") long estacion2) {
        double distancia = estacionService.calularDistanciaEstaciones(estacion1, estacion2);
        return ResponseEntity.ok(distancia);

    }

    @GetMapping("/{idEstacion}")
    public ResponseEntity<Estacion> findEstacionById(@PathVariable("idEstacion") long idEstacion) {
        try {
            Estacion estacion = estacionService.findEstacionById(idEstacion);
            return ResponseEntity.ok(estacion);
        }
        catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
