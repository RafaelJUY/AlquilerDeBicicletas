package com.tpi.bda.microservicioestaciones.repository;

import com.tpi.bda.microservicioestaciones.model.entity.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstacionRepository extends JpaRepository<Estacion, Long> {
    @Query("SELECT COALESCE(MAX(id),0) FROM ESTACIONES ")
    Long getMaxId();
}
