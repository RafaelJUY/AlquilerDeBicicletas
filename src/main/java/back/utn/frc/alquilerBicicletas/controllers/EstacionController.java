package back.utn.frc.alquilerBicicletas.controllers;

import back.utn.frc.alquilerBicicletas.entities.Estacion;
import back.utn.frc.alquilerBicicletas.services.interfaces.EstacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones")
public class EstacionController {

    private final EstacionService estacionService;

    public EstacionController(EstacionService estacionService) {
        this.estacionService = estacionService;
    }


    @GetMapping
    public ResponseEntity<List<Estacion>> getAll() {
        List<Estacion> values = this.estacionService.getAll();
        return ResponseEntity.ok(values);
    }

}
