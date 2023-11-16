package com.tpi.bda.microservicioalquileres.service.impl;

import com.tpi.bda.microservicioalquileres.dto.AlquilerDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;
import com.tpi.bda.microservicioalquileres.exception.personalized.DatosInconsistentesException;
import com.tpi.bda.microservicioalquileres.exception.personalized.EntidadNoExistenteException;
import com.tpi.bda.microservicioalquileres.exception.personalized.ServicioRemotoException;
import com.tpi.bda.microservicioalquileres.model.EstadoAlquiler;
import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.model.entity.Estacion;
import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import com.tpi.bda.microservicioalquileres.repository.IAlquilerRepository;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import com.tpi.bda.microservicioalquileres.service.ITarifaService;
import com.tpi.bda.microservicioalquileres.servicioRemoto.ServicioRemotoEstacion;
import com.tpi.bda.microservicioalquileres.servicioRemoto.ServicioRemotoMoneda;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;


class AlquilerServiceImplTest {

    private IAlquilerService alquilerService;
    private IAlquilerRepository alquilerRepository;
    private ITarifaService tarifaService;
    private ServicioRemotoEstacion servicioRemotoEstacion;
    private ServicioRemotoMoneda servicioRemotoMoneda;

    @BeforeEach
    public void setUp(){
        alquilerRepository = Mockito.mock(IAlquilerRepository.class);
        tarifaService = Mockito.mock(ITarifaService.class);
        servicioRemotoEstacion = Mockito.mock(ServicioRemotoEstacion.class);
        servicioRemotoMoneda = Mockito.mock(ServicioRemotoMoneda.class);
        alquilerService = new AlquilerServiceImpl(alquilerRepository, tarifaService, servicioRemotoEstacion, servicioRemotoMoneda);
    }

    @Test
    void testFindAllConAlquileres() {
        LocalDateTime fecha = LocalDateTime.of(2023, 11, 15, 12, 30);
        Estacion estacion = new Estacion(1L, "UTN", fecha, -31.44296112317501, -64.19409112111947);
        Alquiler alquilerEsperado = new Alquiler(1L, "1", 1, estacion,
                null, fecha, null, 0, null);
        Mockito.when(alquilerRepository.findAll()).thenReturn(List.of(alquilerEsperado));

        List<Alquiler> x = alquilerService.findAll();
        Assertions.assertEquals(List.of(alquilerEsperado), x);
    }

    @Test
    void testFindAllSinAlquileres() {
        Mockito.when(alquilerRepository.findAll()).thenReturn(new ArrayList<>());

        List<Alquiler> alquileres = alquilerService.findAll();
        Assertions.assertTrue(alquileres.isEmpty());
    }

    @Test
    void testIniciarAlquilerEstacionExistente() {
        LocalDateTime fecha = LocalDateTime.of(2023, 11, 15, 12, 30);
        Estacion estacionSimulada = new Estacion(1L, "UTN", fecha, -31.44296112317501, -64.19409112111947);
        Mockito.when(servicioRemotoEstacion.buscarEstacion(anyLong())).thenReturn(estacionSimulada);
        Mockito.when(alquilerRepository.save(any())).thenAnswer(invocation -> {
            // Obtener y devolver el argumento pasado al método save() como parámetro
            Alquiler alquilerGuardado = invocation.getArgument(0);
            return alquilerGuardado;
        });

        Alquiler x = alquilerService.iniciarAlquiler(1L, "1");
        Assertions.assertEquals("1", x.getIdCliente());
        Assertions.assertEquals(estacionSimulada, x.getEstacionRetiro());
        Assertions.assertNotNull(x.getFechaHoraRetiro());
        Assertions.assertEquals(EstadoAlquiler.INICIADO.getId(), x.getEstado());
    }

    @Test
    void testIniciarAlquilerEstacionNoExistente() {
        long idEstacion = 1L;
        Mockito.when(servicioRemotoEstacion.buscarEstacion(idEstacion))
                .thenThrow(new EntidadNoExistenteException("No se pudo encontrar la estación. Estacion id: " + idEstacion));

        EntidadNoExistenteException ex = Assertions.assertThrows(EntidadNoExistenteException.class,
                () -> alquilerService.iniciarAlquiler(1L, "1"));
        Assertions.assertEquals("No se pudo encontrar la estación. Estacion id: " + idEstacion, ex.getMessage());

    }

    @Test
    void testIniciarAlquilerServicioRemotoCaido() {
        // Configuración del mock para simular el comportamiento del servicio remoto cuando el servicio está caído
        Mockito.when(servicioRemotoEstacion.buscarEstacion(anyLong())).thenThrow(new ServicioRemotoException("Error al conectar con servicio de estación"));

        ServicioRemotoException ex = Assertions.assertThrows(ServicioRemotoException.class,
                () -> alquilerService.iniciarAlquiler(1L, "1"));
        Assertions.assertEquals("Error al conectar con servicio de estación", ex.getMessage());
    }

    @Test
    void tetsFinalizarAlquiler() {
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Alquiler alquilerEsperado = new Alquiler(1L, "1", 1, estacion1,
                null, fecha, null, 0, null);
        Mockito.when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquilerEsperado));
        Estacion estacion2 = new Estacion(2L, "Plaza España", null, -31.42851620739036, -64.18475904990146);
        Mockito.when(servicioRemotoEstacion.buscarEstacion(2L)).thenReturn(estacion2);
        Tarifa tarifa = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Mockito.when(tarifaService.getTarifaDeHoy()).thenReturn(tarifa);
        Mockito.when(servicioRemotoEstacion.obtenerDistanciaAEstacionDevolucion(anyLong(),anyLong())).thenReturn(1891.690264670064);
        Mockito.when(alquilerRepository.save(any())).thenAnswer(invocation -> {
            Alquiler alquilerGuardado = invocation.getArgument(0);
            return alquilerGuardado;
        });

        AlquilerDto x = alquilerService.finalizarAlquiler(1L, 2L, "ARS");
        Assertions.assertEquals(620, x.getMonto());
        Assertions.assertEquals(estacion1.getNombre(), x.getEstacionRetiro());
        Assertions.assertEquals(estacion2.getNombre(), x.getEstacionDevolucion());
        Assertions.assertEquals(alquilerEsperado.getFechaHoraRetiro(), x.getFechaHoraRetiro());
        Assertions.assertNotNull(x.getFechaHoraDevolucion());
    }

    @Test
    void tetsFinalizarAlquilerPreviamenteFinalizado() {
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", null, -31.42851620739036, -64.18475904990146);
        Tarifa tarifa = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Alquiler alquilerEsperado = new Alquiler(1L, "1", 2, estacion1,
                estacion2, fecha, LocalDateTime.now(), 620, tarifa);
        Mockito.when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquilerEsperado));

        long idAlquiler = 1L;
        DatosInconsistentesException ex = Assertions.assertThrows(DatosInconsistentesException.class, () -> {
           alquilerService.finalizarAlquiler(idAlquiler, 2L, "ARS");
        });
        Assertions.assertEquals("El alquiler ya fue finalizado previamente Alquiler id: "+idAlquiler, ex.getMessage());
    }

    @Test
    void tetsFinalizarAlquilerMismaEstacionRetiroYDevolucion() {
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Alquiler alquilerEsperado = new Alquiler(1L, "1", 1, estacion1,
                null, fecha, null, 0, null);
        Mockito.when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquilerEsperado));

        long idAlquiler = 1L;
        DatosInconsistentesException ex = Assertions.assertThrows(DatosInconsistentesException.class, () -> {
            alquilerService.finalizarAlquiler(idAlquiler, 1L, "ARS");
        });
        Assertions.assertEquals("La estacion de devolucion no puede ser igual a la de retiro", ex.getMessage());
    }

    @Test
    void testMostrarAlquilerFinalizado() {
        RespuestaConversionDto respuesta = new RespuestaConversionDto("EUR", 1500.50);
        Mockito.when(servicioRemotoMoneda.obtenerConversion(any())).thenReturn(Optional.of(respuesta));
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", null, -31.42851620739036, -64.18475904990146);
        Tarifa tarifa = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Alquiler alquiler = new Alquiler(1L, "1", 2, estacion1,
                estacion2, fecha, LocalDateTime.now(), 620, tarifa);


        AlquilerDto x = alquilerService.mostrarAlquilerFinalizado(alquiler, "EUR");
        Assertions.assertEquals(alquiler.getEstacionRetiro().getNombre(), x.getEstacionRetiro());
        Assertions.assertEquals(alquiler.getEstacionDevolucion().getNombre(), x.getEstacionDevolucion());
        Assertions.assertEquals(alquiler.getFechaHoraRetiro(), x.getFechaHoraRetiro());
        Assertions.assertEquals(alquiler.getFechaHoraDevolucion(), x.getFechaHoraDevolucion());
        Assertions.assertEquals("EUR", x.getMoneda());
        Assertions.assertEquals(1500.50, x.getMonto());
    }

    @Test
    void testMostrarAlquilerFinalizadoFallaServicioConversion(){
        Mockito.when(servicioRemotoMoneda.obtenerConversion(any())).thenReturn(Optional.empty());
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", null, -31.42851620739036, -64.18475904990146);
        Tarifa tarifa = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Alquiler alquiler = new Alquiler(1L, "1", 2, estacion1,
                estacion2, fecha, LocalDateTime.now(), 620, tarifa);


        AlquilerDto x = alquilerService.mostrarAlquilerFinalizado(alquiler, "EUR");
        Assertions.assertEquals(alquiler.getEstacionRetiro().getNombre(), x.getEstacionRetiro());
        Assertions.assertEquals(alquiler.getEstacionDevolucion().getNombre(), x.getEstacionDevolucion());
        Assertions.assertEquals(alquiler.getFechaHoraRetiro(), x.getFechaHoraRetiro());
        Assertions.assertEquals(alquiler.getFechaHoraDevolucion(), x.getFechaHoraDevolucion());
        Assertions.assertEquals("ARS", x.getMoneda());
        Assertions.assertEquals(620, x.getMonto());
    }

    @Test
    void testMostrarAlquilerFinalizadoMonedaInvalida() {
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", null, -31.42851620739036, -64.18475904990146);
        Tarifa tarifa = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Alquiler alquiler = new Alquiler(1L, "1", 2, estacion1,
                estacion2, fecha, LocalDateTime.now(), 620, tarifa);

        AlquilerDto x = alquilerService.mostrarAlquilerFinalizado(alquiler, "AAA");
        Assertions.assertEquals(alquiler.getEstacionRetiro().getNombre(), x.getEstacionRetiro());
        Assertions.assertEquals(alquiler.getEstacionDevolucion().getNombre(), x.getEstacionDevolucion());
        Assertions.assertEquals(alquiler.getFechaHoraRetiro(), x.getFechaHoraRetiro());
        Assertions.assertEquals(alquiler.getFechaHoraDevolucion(), x.getFechaHoraDevolucion());
        Assertions.assertEquals("ARS", x.getMoneda());
        Assertions.assertEquals(alquiler.getMonto(), x.getMonto());
    }

    @Test
    void testFindByIdAlquilerExistente() {
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Alquiler alquilerEsperado = new Alquiler(1L, "1", 1, estacion1,
                null, fecha, null, 0, null);
        Mockito.when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquilerEsperado));

        Alquiler x = alquilerService.findById(1L);
        Assertions.assertEquals(alquilerEsperado, x);
    }

    @Test
    void testFindByIdAlquilerNoExistente() {
        Mockito.when(alquilerRepository.findById(anyLong())).thenThrow(new EntidadNoExistenteException());

        Assertions.assertThrows(EntidadNoExistenteException.class, () -> {
            alquilerService.findById(1L);
        });
    }

    @Test
    void testObtenerAlquileresPorMontos() {
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", null, -31.42851620739036, -64.18475904990146);
        Tarifa tarifa = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Alquiler alquiler = new Alquiler(1L, "1", 2, estacion1,
                estacion2, fecha, LocalDateTime.now(), 620, tarifa);
        Mockito.when(alquilerService.obtenerAlquileresPorMontos(anyDouble(),anyDouble())).thenReturn(List.of(alquiler));

        List<Alquiler> x = alquilerService.obtenerAlquileresPorMontos(500,650);
        Assertions.assertEquals(List.of(alquiler), x);
    }

    @Test
    void testObtenerAlquileresPorMontosParametrosInvalidos(){
        LocalDateTime fecha = LocalDateTime.now().minusHours(1);
        Estacion estacion1 = new Estacion(1L, "UTN", null, -31.44296112317501, -64.19409112111947);
        Estacion estacion2 = new Estacion(2L, "Plaza España", null, -31.42851620739036, -64.18475904990146);
        Tarifa tarifa = new Tarifa(1L, 1, 'S', 1, null, null, null, 300, 6, 80, 240);
        Alquiler alquiler = new Alquiler(1L, "1", 2, estacion1,
                estacion2, fecha, LocalDateTime.now(), 620, tarifa);
        Mockito.when(alquilerService.obtenerAlquileresPorMontos(anyDouble(),anyDouble())).thenReturn(List.of(alquiler));

        // monto minimo mayor a monto maximo
        List<Alquiler> x = alquilerService.obtenerAlquileresPorMontos(650,500);
        Assertions.assertTrue(x.isEmpty());
    }

    @Test
    void testObtenerAlquileresPorMontosSinAlquileresExistentes() {
        Mockito.when(alquilerRepository.findByMontoBetween(anyDouble(), anyDouble())).thenReturn(new ArrayList<>());

        List<Alquiler> x = alquilerService.obtenerAlquileresPorMontos(0,1000);
        Assertions.assertTrue(x.isEmpty());
    }
}