package com.tpi.bda.microservicioalquileres.controller;

import com.tpi.bda.microservicioalquileres.dto.AlquilerDto;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {
    private final IAlquilerService alquilerService;

    public AlquilerController(IAlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping
    public ResponseEntity<List<Alquiler>> findAll() {
        List<Alquiler> values = alquilerService.findAll();
        return ResponseEntity.ok(values);
    }

    @PostMapping
    public ResponseEntity<Alquiler> iniciarAlquiler(@RequestParam long idEstacion,
                                                    @RequestParam String idCliente) {

        Alquiler alquiler = alquilerService.iniciarAlquiler(idEstacion, idCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(alquiler);
    }

    @PatchMapping
    public ResponseEntity<AlquilerDto> finalizarAlquiler(@RequestParam("idAlquiler") long idAlquiler,
                                                         @RequestParam("idEstacion") long idEstacion,
                                                         @RequestParam(value = "moneda", defaultValue = "ARS") String moneda){

        AlquilerDto alquilerDto = alquilerService.finalizarAlquiler(idAlquiler, idEstacion, moneda);
        return ResponseEntity.status(HttpStatus.OK).body(alquilerDto);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Alquiler>> obtenerAlquileresPorMontos(@RequestParam("montoMin") double montoMin,
                                                                     @RequestParam("montoMax") double montoMax){
        List<Alquiler> alquileres = alquilerService.obtenerAlquileresPorMontos(montoMin, montoMax);
        if (alquileres.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alquileres);
        }
        return ResponseEntity.ok(alquileres);
    }

}


