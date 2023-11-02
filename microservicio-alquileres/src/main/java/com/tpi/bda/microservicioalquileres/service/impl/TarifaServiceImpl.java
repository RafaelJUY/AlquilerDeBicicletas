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

    public List<Tarifa> findByDefinicion(char definicion) {
        return this.tarifaRepository.findTarifaByDefinicion(definicion);
    }

    public Tarifa getTarifaDeHoy(){
        int diaHoy = LocalDateTime.now().getDayOfMonth();
        int mesHoy = LocalDateTime.now().getMonth().getValue();
        int anioHoy = LocalDateTime.now().getYear();
        int diaDeLaSemana = LocalDateTime.now().getDayOfWeek().getValue();

        List<Tarifa> tarifasConDescuento = this.findByDefinicion('C');
        List<Tarifa> tarifasSinDescuento = this.findByDefinicion('S');

        Tarifa tarifa = tarifasConDescuento
                .stream()
                .filter(t -> t.getDiaMes() == diaHoy &&
                        t.getMes() == mesHoy &&
                        t.getAnio() == anioHoy).findFirst().orElse(null);

        if (tarifa != null) {
            // con descuento
            return tarifa;
        } else {
            // sin descuento
            tarifa = tarifasSinDescuento.stream().filter(t -> t.getDiaSemana() == diaDeLaSemana).findFirst().orElse(null);
            return tarifa;
        }

    }
}
