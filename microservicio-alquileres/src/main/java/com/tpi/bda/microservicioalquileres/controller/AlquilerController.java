package com.tpi.bda.microservicioalquileres.controller;

import com.tpi.bda.microservicioalquileres.dto.AlquilerDto;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.List;
import java.util.NoSuchElementException;

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
        try {
            Alquiler alquiler = alquilerService.iniciarAlquiler(idEstacion, idCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(alquiler);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (ResourceAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }


    }


    @PatchMapping
    public ResponseEntity<AlquilerDto> finalizarAlquiler(@RequestParam("idAlquiler") long idAlquiler,
                                                         @RequestParam("idEstacion") long idEstacion,
                                                         @RequestParam("moneda") String moneda){
        try {
            AlquilerDto alquiler = alquilerService.finalizarAlquiler(idAlquiler, idEstacion, moneda);
            if (alquiler == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            return ResponseEntity.ok(alquiler);
        }
        catch (ResourceAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }


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


