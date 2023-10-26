package back.utn.frc.alquilerBicicletas.repositories;

import back.utn.frc.alquilerBicicletas.entities.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
}
