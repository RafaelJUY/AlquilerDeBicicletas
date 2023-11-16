package com.tpi.bda.microservicioalquileres.service.impl;

import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import com.tpi.bda.microservicioalquileres.repository.ITarifaRepository;
import com.tpi.bda.microservicioalquileres.service.ITarifaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

class TarifaServiceImplTest {

    private ITarifaRepository tarifaRepository;
    private TarifaServiceImpl tarifaService;

    @BeforeEach
    public void setUp() {
        tarifaRepository = Mockito.mock(ITarifaRepository.class);
        tarifaService = new TarifaServiceImpl(tarifaRepository);
    }

    @Test
    void testGetAll() {
        Tarifa tarifaEsperada = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Mockito.when(tarifaRepository.findAll()).thenReturn(List.of(tarifaEsperada));

        List<Tarifa> x = tarifaService.getAll();
        Assertions.assertEquals(List.of(tarifaEsperada), x);
    }

    @Test
    void testGetTarifaDeHoyDiaSinDescuento() {
        Tarifa tarifaEsperada = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Mockito.when(tarifaRepository.obtnerTarifaSinDescuento(anyInt())).thenReturn(tarifaEsperada);
        Mockito.when(tarifaRepository.obtenerTarifaConDescuento(anyInt(), anyInt(), anyInt())).thenReturn(null);

        Tarifa x = tarifaService.getTarifaDeHoy();
        Assertions.assertEquals(tarifaEsperada, x);
    }

    @Test
    void testGetTarifaDeHoyDiaConDescuento() {
        Tarifa tarifaEsperada = new Tarifa(1L, 2, 'C', null, 15, 11, 2023, 200, 4, 75, 175);
        Mockito.when(tarifaRepository.obtenerTarifaConDescuento(anyInt(), anyInt(), anyInt())).thenReturn(tarifaEsperada);

        Tarifa x = tarifaService.getTarifaDeHoy();
        Assertions.assertEquals(tarifaEsperada, x);
    }
}