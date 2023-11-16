package com.tpi.bda.microservicioalquileres.service.impl;

import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import org.springframework.stereotype.Service;

import com.tpi.bda.microservicioalquileres.repository.ITarifaRepository;
import com.tpi.bda.microservicioalquileres.service.ITarifaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarifaServiceImpl implements ITarifaService {
    private final ITarifaRepository tarifaRepository;

    public TarifaServiceImpl(ITarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    @Override
    public List<Tarifa> getAll() {
        return this.tarifaRepository.findAll();
    }

    @Override
    public Tarifa getTarifaDeHoy(){
        Tarifa tarifaDeHoy;
        LocalDateTime fechaHoy = LocalDateTime.now();

        int diaHoy = fechaHoy.getDayOfMonth();
        int mesHoy = fechaHoy.getMonth().getValue();
        int anioHoy = fechaHoy.getYear();

        tarifaDeHoy = this.obtenerTarifaConDescuento(diaHoy, mesHoy, anioHoy);
        if (tarifaDeHoy == null){
            int diaDeLaSemana = fechaHoy.getDayOfWeek().getValue();
            tarifaDeHoy = this.obtenerTarifaSinDescuento(diaDeLaSemana);
        }
        return tarifaDeHoy;
    }

    private Tarifa obtenerTarifaConDescuento(int dia, int mes, int anio){
        return tarifaRepository.obtenerTarifaConDescuento(dia, mes, anio);
    }

    private Tarifa obtenerTarifaSinDescuento(int diaSemana){
        return tarifaRepository.obtnerTarifaSinDescuento(diaSemana);
    }
}
