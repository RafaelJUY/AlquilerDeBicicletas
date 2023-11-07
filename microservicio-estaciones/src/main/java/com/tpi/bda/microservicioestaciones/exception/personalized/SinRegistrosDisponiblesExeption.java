package com.tpi.bda.microservicioestaciones.exception.personalized;

public class SinRegistrosDisponiblesExeption extends RuntimeException{
    public SinRegistrosDisponiblesExeption() {
        super("La Base de Datos no tiene ningún registro cargado para completar la operación");
    }

    public SinRegistrosDisponiblesExeption(String message) {
        super(message);
    }
}
