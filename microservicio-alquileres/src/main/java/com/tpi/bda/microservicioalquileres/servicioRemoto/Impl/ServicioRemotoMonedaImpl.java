package com.tpi.bda.microservicioalquileres.servicioRemoto.Impl;

import com.tpi.bda.microservicioalquileres.dto.ConversionDto;
import com.tpi.bda.microservicioalquileres.dto.RespuestaConversionDto;
import com.tpi.bda.microservicioalquileres.servicioRemoto.ServicioRemotoMoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ServicioRemotoMonedaImpl implements ServicioRemotoMoneda {
    @Autowired
    private RestTemplate rt;

    private final String urlBase = "http://34.82.105.125:8080";

    public Optional<RespuestaConversionDto> obtenerConversion(ConversionDto conversion) {
        Optional<RespuestaConversionDto> respuestaOp = Optional.empty();
        try {
            // Respuesta de la peticion
            ResponseEntity<RespuestaConversionDto> res = rt.postForEntity(
                    urlBase + "/convertir", conversion, RespuestaConversionDto.class);

            // Se comprueba si el código de repuesta es de la familia 200
            if (res.getStatusCode().is2xxSuccessful()) {
                respuestaOp = Optional.ofNullable(res.getBody());
            }
        } catch (HttpClientErrorException ex) { //Si la moneda no es aceptada por el servicio de conversión
            respuestaOp = Optional.empty();
        } catch (ResourceAccessException ex){ // Si el servicio de conversión de moneda no esta en ejecución
            respuestaOp = Optional.empty();
        }
        return respuestaOp;
    }
}
