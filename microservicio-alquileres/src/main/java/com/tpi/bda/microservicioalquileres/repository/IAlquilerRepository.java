package com.tpi.bda.microservicioalquileres.repository;

import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAlquilerRepository extends JpaRepository<Alquiler, Long> {
    List<Alquiler> findByMontoBetween(double montoMin, double montoMax);

}
