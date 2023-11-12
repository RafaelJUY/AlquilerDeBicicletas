package com.tpi.bda.microservicioalquileres.exception.personalized;

public class EntidadNoExistenteException extends RuntimeException{
    public EntidadNoExistenteException() {
        super("La entidad no existe en Base de Datos");
    }

    public EntidadNoExistenteException(String message) {
        super(message);
    }
}