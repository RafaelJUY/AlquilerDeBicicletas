package com.tpi.bda.microservicioestaciones.cotroller;

import com.tpi.bda.microservicioestaciones.dto.EstacionDto;
import com.tpi.bda.microservicioestaciones.model.Ubicacion;
import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import com.tpi.bda.microservicioestaciones.service.IEstacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tpi.bda.microservicioestaciones.service.convert.EstacionEntityDto.*;

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
        Estacion estacionCercana = this.estacionService.findEstacionCercana(new Ubicacion(latitud, longitud));
        return ResponseEntity.ok(estacionCercana);

    }

    @GetMapping("/distanciaEntreEstaciones/{idEstacion1}/{idEstacion2}")
    public ResponseEntity<Double> calularDistanciaEstaciones(@PathVariable("idEstacion1") long idEstacion1,
                                                             @PathVariable("idEstacion2") long idEstacion2) {
        double distancia = estacionService.calularDistancia(estacionService.findEstacionById(idEstacion1),
                    estacionService.findEstacionById(idEstacion2));
        return ResponseEntity.ok(distancia);
    }

    @GetMapping("/{idEstacion}")
    public ResponseEntity<Estacion> findEstacionById(@PathVariable("idEstacion") long idEstacion) {
        Estacion estacion = estacionService.findEstacionById(idEstacion);
        return ResponseEntity.ok(estacion);
    }

    @PostMapping
    public ResponseEntity<Estacion> crearEstacion(@Valid @RequestBody EstacionDto estacionDto){
        Estacion estacion = estacionDtoAEntidad(estacionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(estacionService.crearEstacion(estacion));
    }
}
