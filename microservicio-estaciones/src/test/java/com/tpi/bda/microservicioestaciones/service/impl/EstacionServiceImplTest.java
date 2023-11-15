package com.tpi.bda.microservicioestaciones.service.impl;

import com.tpi.bda.microservicioestaciones.exception.personalized.SinRegistrosDisponiblesException;
import com.tpi.bda.microservicioestaciones.model.Ubicacion;
import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import com.tpi.bda.microservicioestaciones.repository.IEstacionRepository;
import com.tpi.bda.microservicioestaciones.service.IEstacionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


class EstacionServiceImplTest {

    private IEstacionService estacionService;
    private IEstacionRepository estacionRepository;

    @BeforeEach
    public void setup() {
        estacionRepository = Mockito.mock(IEstacionRepository.class);
        estacionService = new EstacionServiceImpl(estacionRepository);
    }

    @Test
    void testFindAllEstacionesExistentes() {
        LocalDateTime fecha = LocalDateTime.of(2023, 11, 15, 12, 30);
        Estacion estacionEsperada = new Estacion(1L, "UTN", fecha, -31.44296112317501, -64.19409112111947);
        Mockito.when(estacionRepository.findAll()).thenReturn(List.of(estacionEsperada));

        List<Estacion> x = estacionService.findAll();
        Assertions.assertEquals(List.of(estacionEsperada), x);
    }

    @Test
    void testFindAllSinEstaciones() {
        Mockito.when(estacionRepository.findAll()).thenReturn(new ArrayList<Estacion>());

        List<Estacion> estaciones = estacionService.findAll();
        Assertions.assertTrue(estaciones.isEmpty());
    }

    @Test
    void testFindEstacionCercana() {
        LocalDateTime fecha = LocalDateTime.of(2023, 11, 15, 12, 30);
        Estacion estacion1 = new Estacion(1L, "UTN", fecha, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", fecha, -31.42851620739036, -64.18475904990146);
        List<Estacion> estaciones = new ArrayList<Estacion>();
        estaciones.add(estacion1);
        estaciones.add(estacion2);
        Mockito.when(estacionRepository.findAll()).thenReturn(estaciones);

        Estacion estacionMasCercana = estacionService.findEstacionCercana(new Ubicacion(-31.45071308753589, -64.20038276002808)); // Rotonda Almirante Guillermo Brown (Barrio Las Flores)
        Assertions.assertEquals(estacionMasCercana, estacion1);
    }

    @Test
    void testFindEstacionCercanaSinEstaciones() {
        Mockito.when(estacionRepository.findAll()).thenReturn(new ArrayList<Estacion>());

        // assertThrows permite verificar que el método lanza la excepción esperada
        assertThrows(SinRegistrosDisponiblesException.class, () -> {
            estacionService.findEstacionCercana(new Ubicacion(-31.45071308753589, -64.20038276002808));
        });
    }

    @Test
    void testCalularDistanciaEntreEstaciones() {
        LocalDateTime fecha = LocalDateTime.of(2023, 11, 15, 12, 30);
        Estacion estacion1 = new Estacion(1L, "UTN", fecha, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", fecha, -31.42851620739036, -64.18475904990146);

        double distancia = estacionService.calularDistancia(estacion1, estacion2);
        double delta = 0.1;
        Assertions.assertEquals(1891.690264670064, distancia, delta); // 1891.690264670064 distancia entre plaza españa y utn
    }

    @Test
    void testCalularDistanciaEntreUbicacionYEstacion() {
    }

    @Test
    void findEstacionById() {
    }

    @Test
    void crearEstacion() {
    }
}