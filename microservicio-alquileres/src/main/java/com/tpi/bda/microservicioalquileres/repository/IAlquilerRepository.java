package com.tpi.bda.microservicioalquileres.repository;

import com.tpi.bda.microservicioalquileres.model.entity.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlquilerRepository extends JpaRepository<Alquiler, Long> {
}
