package back.utn.frc.alquilerBicicletas.services;

import back.utn.frc.alquilerBicicletas.entities.Alquiler;
import back.utn.frc.alquilerBicicletas.repositories.AlquilerRepository;
import back.utn.frc.alquilerBicicletas.services.interfaces.AlquilerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlquilerServiceImpl implements AlquilerService {

    private final AlquilerRepository alquilerRepository;

    public AlquilerServiceImpl(AlquilerRepository alquilerRepository) {
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