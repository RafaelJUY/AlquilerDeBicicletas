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
    public ResponseEntity<List<Estacion>> findAll() {
        List<Estacion> values = this.estacionService.findAll();
        return ResponseEntity.ok(values);
    }

    @GetMapping("/masCercana")
    public ResponseEntity<Estacion> findEstacionCercana(@RequestParam("latitud") double latitud,
                                                        @RequestParam("longitud") double longitud) {
        Estacion estacionCercana = this.estacionService.findEstacionCercana(latitud, longitud);
        return ResponseEntity.ok(estacionCercana);

    }

}
