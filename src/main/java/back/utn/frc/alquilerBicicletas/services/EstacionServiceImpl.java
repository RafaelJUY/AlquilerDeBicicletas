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
    public List<Estacion> getAll() {
        return this.estacionRepository.findAll();
    }
}
