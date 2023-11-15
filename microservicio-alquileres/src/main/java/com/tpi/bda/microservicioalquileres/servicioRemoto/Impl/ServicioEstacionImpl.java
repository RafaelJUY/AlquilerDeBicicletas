package com.tpi.bda.microservicioalquileres.servicioRemoto.Impl;

import com.tpi.bda.microservicioalquileres.exception.personalized.EntidadNoExistenteException;
import com.tpi.bda.microservicioalquileres.exception.personalized.ServicioRemotoException;
import com.tpi.bda.microservicioalquileres.model.entity.Estacion;
import com.tpi.bda.microservicioalquileres.servicioRemoto.ServicioRemotoEstacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@Service
public class ServicioEstacionImpl implements ServicioRemotoEstacion {
    @Autowired
    private RestTemplate rt;

    private final String urlBase = "http://localhost:8001/api/estaciones";

    public Estacion buscarEstacion(Long idEstacion){
        Optional<Estacion> estacionOp;
        // Creación de una instancia de RestTemplate
        try {
            // Creación de la instancia de RequestTemplate
//            RestTemplate template = new RestTemplate();

            // Creación de la entidad a enviar
            ResponseEntity<Estacion> res = rt.getForEntity(
                    urlBase +"/{idEstacion}", Estacion.class, idEstacion
            );
            estacionOp = Optional.ofNullable(res.getBody());
        } catch (HttpClientErrorException ex) { // En caso de no encontrar la estación
            estacionOp = Optional.empty();
        } catch (ResourceAccessException ex){ // Si el servicio de estación no esta en ejecución
            throw new ServicioRemotoException("Error al conectar con servicio de estación");
        }

        return estacionOp.orElseThrow(() -> new EntidadNoExistenteException("No se pudo encontrar la estación. " +
                "\nEstacion id: " + idEstacion));
    }

    public Double obtenerDistanciaAEstacionDevolucion(Long idEstacionRetiro, Long idEstacionDevolucion) {
        // Creación de una instancia de RestTemplate
        try {
            // Creación de la instancia de RequestTemplate
//            RestTemplate template = new RestTemplate();

            // parametros
            HashMap<String, String> params = new HashMap<>();
            params.put("estacion1", idEstacionRetiro.toString());
            params.put("estacion2", idEstacionDevolucion.toString());

            // Creación de la entidad a enviar
            String url = urlBase + "/distanciaEntreEstaciones/{estacion1}/{estacion2}";
            ResponseEntity<Double> res = rt.getForEntity(
                    url, Double.class, params
            );
            return res.getBody();

        } catch (HttpClientErrorException ex) { // Si la estación no existe.
            throw new EntidadNoExistenteException("La estacion de devolucion no existe: \nid estacion = " + idEstacionDevolucion);
        }catch (ResourceAccessException ex){ // Si el servicio de estación no esta en ejecución
            throw new ServicioRemotoException("Error al conectar con servicio de estación");
        }
    }
}
