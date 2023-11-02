package com.tpi.bda.microservicioestaciones.cotroller;

import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import com.tpi.bda.microservicioestaciones.service.IEstacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
