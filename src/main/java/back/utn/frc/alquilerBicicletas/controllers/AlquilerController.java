package back.utn.frc.alquilerBicicletas.controllers;

import back.utn.frc.alquilerBicicletas.entities.Alquiler;
import back.utn.frc.alquilerBicicletas.services.interfaces.AlquilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping
    public ResponseEntity<List<Alquiler>> findAll() {
        List<Alquiler> values = alquilerService.findAll();
        return ResponseEntity.ok(values);

    }
}
