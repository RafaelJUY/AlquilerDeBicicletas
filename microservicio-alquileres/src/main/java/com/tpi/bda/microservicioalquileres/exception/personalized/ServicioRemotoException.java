package com.tpi.bda.microservicioalquileres.exception.personalized;

public class ServicioRemotoException extends RuntimeException{
    public ServicioRemotoException() {
        super("Error al conectar con el servicio remoto");
    }

    public ServicioRemotoException(String message) {
        super(message);
    }
}
