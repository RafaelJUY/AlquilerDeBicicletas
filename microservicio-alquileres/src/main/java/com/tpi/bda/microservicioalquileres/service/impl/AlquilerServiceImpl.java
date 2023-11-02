package com.tpi.bda.microservicioalquileres.service.impl;

import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import com.tpi.bda.microservicioalquileres.repository.IAlquilerRepository;
import com.tpi.bda.microservicioalquileres.service.IAlquilerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlquilerServiceImpl implements IAlquilerService {
    private final IAlquilerRepository alquilerRepository;

    public AlquilerServiceImpl(IAlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
    }

    @Override
    public List<Alquiler> findAll() {
        return alquilerRepository.findAll();
    }

    @Override
    public Alquiler iniciarAlquiler(long idEstacion) {


        return null;
    }
}
