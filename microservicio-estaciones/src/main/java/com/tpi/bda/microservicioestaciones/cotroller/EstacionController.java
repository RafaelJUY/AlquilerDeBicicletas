package com.tpi.bda.microservicioestaciones.cotroller;

import com.tpi.bda.microservicioestaciones.dto.EstacionDto;
import com.tpi.bda.microservicioestaciones.model.Ubicacion;
import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import com.tpi.bda.microservicioestaciones.service.IEstacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

//    @GetMapping("/masCercana")
//    public ResponseEntity<Estacion> findEstacionCercana(@RequestParam("latitud") double latitud,
//                                                        @RequestParam("longitud") double longitud) {
//        Estacion estacionCercana = this.estacionService.findEstacionCercana(latitud, longitud);
//        return ResponseEntity.ok(estacionCercana);
//
//    }

    /*
    Error al querer recibir latitud y longitud en un JSON de la clase Ubicacion usando @RequestBody
    Por alguna razon funciona con:
    io.swagger.v3.oas.annotations.parameters.RequestBody

    pero no con:
    org.springframework.web.bind.annotation.RequestBody

    El segundo es el que estamos usando para el metodo "crearEstacion" y funciona sin problemas.
    Por tal motivo se deja latitud y longitud como valores separados.
    */
    @GetMapping("/masCercana")
    public ResponseEntity<Estacion> findEstacionCercana(@RequestParam("latitud") double latitud,
                                                        @RequestParam("longitud") double longitud) {
        Estacion estacionCercana = this.estacionService.findEstacionCercana(new Ubicacion(latitud, longitud));
        return ResponseEntity.ok(estacionCercana);

    }

//    @GetMapping("/distanciaEntreEstaciones/{estacion1}/{estacion2}")
//    public ResponseEntity<Double> calularDistanciaEstaciones(@PathVariable("estacion1") long estacion1,
//                                                             @PathVariable("estacion2") long estacion2) {
//        double distancia = estacionService.calularDistanciaEstaciones(estacion1, estacion2);
//        return ResponseEntity.ok(distancia);
//
//    }

    @GetMapping("/distanciaEntreEstaciones/{idEstacion1}/{idEstacion2}")
    public ResponseEntity<Double> calularDistanciaEstaciones(@PathVariable("idEstacion1") long idEstacion1,
                                                             @PathVariable("idEstacion2") long idEstacion2) {
        try {
            double distancia = estacionService.calularDistancia(estacionService.findEstacionById(idEstacion1),
                    estacionService.findEstacionById(idEstacion2));
            return ResponseEntity.ok(distancia);
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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

    @PostMapping
    public ResponseEntity<Estacion> crearEstacion(@RequestBody EstacionDto estacionDto){
        Estacion estacion = estacionDtoAEntidad(estacionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(estacionService.crearEstacion(estacion));
    }
}
