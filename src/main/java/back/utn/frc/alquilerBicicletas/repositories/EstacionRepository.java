package back.utn.frc.alquilerBicicletas.repositories;

import back.utn.frc.alquilerBicicletas.entities.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacionRepository extends JpaRepository<Estacion, Long> {
}
