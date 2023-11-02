package com.tpi.bda.microservicioalquileres.repository;

import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITarifaRepository extends JpaRepository<Tarifa, Long> {

    List<Tarifa> findTarifaByDefinicion(char definicion);
}
