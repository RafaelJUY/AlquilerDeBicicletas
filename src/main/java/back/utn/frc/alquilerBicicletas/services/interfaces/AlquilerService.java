package back.utn.frc.alquilerBicicletas.services.interfaces;

import back.utn.frc.alquilerBicicletas.entities.Alquiler;
import java.util.List;

public interface AlquilerService {

    List<Alquiler> findAll();

    Alquiler iniciarAlquiler(long idEstacion);
}
