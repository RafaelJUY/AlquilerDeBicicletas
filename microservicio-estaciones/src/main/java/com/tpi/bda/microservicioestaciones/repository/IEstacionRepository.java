package com.tpi.bda.microservicioestaciones.repository;

import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstacionRepository extends JpaRepository<Estacion, Long> {
}
