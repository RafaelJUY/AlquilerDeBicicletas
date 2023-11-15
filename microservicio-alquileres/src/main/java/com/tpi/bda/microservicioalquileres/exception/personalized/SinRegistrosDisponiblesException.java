package com.tpi.bda.microservicioalquileres.exception.personalized;

public class SinRegistrosDisponiblesException extends RuntimeException{
    public SinRegistrosDisponiblesException() {
        super("La Base de Datos no tiene ningún registro cargado para completar la operación");
    }

    public SinRegistrosDisponiblesException(String message) {
        super(message);
    }
}
