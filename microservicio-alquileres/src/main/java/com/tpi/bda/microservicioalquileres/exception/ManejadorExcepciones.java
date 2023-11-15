package com.tpi.bda.microservicioalquileres.exception;

import com.tpi.bda.microservicioalquileres.exception.personalized.DatosInconsistentesException;
import com.tpi.bda.microservicioalquileres.exception.personalized.EntidadNoExistenteException;
import com.tpi.bda.microservicioalquileres.exception.personalized.ServicioRemotoException;
import com.tpi.bda.microservicioalquileres.exception.personalized.SinRegistrosDisponiblesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorExcepciones {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejadorExeption(Exception ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(EntidadNoExistenteException.class)
    public ResponseEntity<String> manejadorEntidadNoExistenteException(EntidadNoExistenteException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SinRegistrosDisponiblesException.class)
    public ResponseEntity<String> manejadorSinRegistrosDisponiblesExeption(SinRegistrosDisponiblesException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ServicioRemotoException.class)
    public ResponseEntity<String> manejadorServicioRemotoException(ServicioRemotoException ex){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

    @ExceptionHandler(DatosInconsistentesException.class)
    public ResponseEntity<String> manejadorDatosInconsistentesException(DatosInconsistentesException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
