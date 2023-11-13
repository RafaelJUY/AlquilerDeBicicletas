package com.tpi.bda.microservicioalquileres.servicioRemoto.Impl;

import com.tpi.bda.microservicioalquileres.dto.ConversionDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;
import com.tpi.bda.microservicioalquileres.exception.personalized.ServicioRemotoException;
import com.tpi.bda.microservicioalquileres.servicioRemoto.ServicioRemotoMoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ServicioRemotoMonedaImpl implements ServicioRemotoMoneda {
    @Autowired
    private RestTemplate rt;

    private final String urlBase = "http://34.82.105.125:8080";
    public RespuestaConversionDto obtenerConversion(ConversionDto conversion) {
        try {
            // Creación de la instancia de RequestTemplate
//            RestTemplate template = new RestTemplate();

            // Respuesta de la peticion
            ResponseEntity<RespuestaConversionDto> res = rt.postForEntity(urlBase + "/convertir", conversion, RespuestaConversionDto.class);

            // Se comprueba si el código de repuesta es de la familia 200
            if (res.getStatusCode().is2xxSuccessful()) {
                //log.debug("Persona creada correctamente: {}", res.getBody());
                return res.getBody();
            }
        } catch (HttpClientErrorException ex) {
            throw new ServicioRemotoException("Moneda no aceptada para conversion");
        }
        return null;
    }
}
