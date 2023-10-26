package back.utn.frc.alquilerBicicletas.services.interfaces;

import back.utn.frc.alquilerBicicletas.entities.Estacion;

import java.util.List;

public interface EstacionService {
    List<Estacion> findAll();

    Estacion findEstacionCercana(double latitud, double longitud);
}
