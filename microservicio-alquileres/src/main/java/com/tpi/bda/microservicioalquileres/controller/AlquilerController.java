package com.tpi.bda.microservicioalquileres.controller;

import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
