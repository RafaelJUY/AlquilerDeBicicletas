package back.utn.frc.alquilerBicicletas.services;

import back.utn.frc.alquilerBicicletas.entities.Estacion;
import back.utn.frc.alquilerBicicletas.repositories.EstacionRepository;
import back.utn.frc.alquilerBicicletas.services.interfaces.EstacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstacionServiceImpl implements EstacionService {
    private final EstacionRepository estacionRepository;

    public EstacionServiceImpl(EstacionRepository estacionRepository) {
        this.estacionRepository = estacionRepository;
    }

    @Override
    public List<Estacion> findAll() {
        return this.estacionRepository.findAll();
    }

    @Override
    public Estacion findEstacionCercana(double latitud, double longitud) {
        List<Estacion> estaciones = this.findAll();
        Estacion estacionCercana = estaciones.get(0);
        double menorDistancia = 110000 * Math.sqrt(Math.pow(estacionCercana.getLatitud() - latitud, 2) +
                Math.pow(estacionCercana.getLongitud() - longitud,2));

        for (Estacion estacion : estaciones) {
            double distancia = 110000 * Math.sqrt(Math.pow(estacion.getLatitud() - latitud, 2) +
                    Math.pow(estacion.getLongitud() - longitud,2));

            if (distancia < menorDistancia) {
                estacionCercana = estacion;
                menorDistancia = distancia;
            }
        }

        return estacionCercana;
    }
}
