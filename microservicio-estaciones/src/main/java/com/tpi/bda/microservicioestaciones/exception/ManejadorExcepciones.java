package com.tpi.bda.microservicioestaciones.exception;


import com.tpi.bda.microservicioestaciones.exception.personalized.EntidadNoExistenteException;
import com.tpi.bda.microservicioestaciones.exception.personalized.SinRegistrosDisponiblesExeption;
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

    @ExceptionHandler(SinRegistrosDisponiblesExeption.class)
    public ResponseEntity<String> manejadorSinRegistrosDisponiblesExeption(SinRegistrosDisponiblesExeption ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
