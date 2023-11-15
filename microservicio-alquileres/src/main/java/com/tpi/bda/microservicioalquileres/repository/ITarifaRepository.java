package com.tpi.bda.microservicioalquileres.repository;

import com.tpi.bda.microservicioalquileres.model.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITarifaRepository extends JpaRepository<Tarifa, Long> {

    List<Tarifa> findTarifaByDefinicion(char definicion);

    @Query("FROM TARIFAS t WHERE t.definicion = 'C' AND t.diaMes = :dia AND t.mes = :mes AND t.anio = :anio")
    Tarifa obtenerTarifaConDescuento(@Param("dia") Integer dia,
                                     @Param("mes") Integer mes,
                                     @Param("anio") Integer anio);
    @Query("FROM TARIFAS t WHERE t.definicion = 'S' AND t.diaSemana = :diaSemana")
    Tarifa obtnerTarifaSinDescuento(@Param("diaSemana") int diaSemana);

}
