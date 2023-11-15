package com.tpi.bda.microservicioalquileres.exception.personalized;

public class DatosInconsistentesException extends RuntimeException{
    public DatosInconsistentesException() {
        super("Los datos son inconsistentes");
    }

    public DatosInconsistentesException(String mensaje) {
        super(mensaje);
    }

}
