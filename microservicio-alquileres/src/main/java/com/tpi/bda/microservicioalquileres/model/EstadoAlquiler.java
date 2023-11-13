package com.tpi.bda.microservicioalquileres.model;

public enum EstadoAlquiler {
    INICIADO(1),
    FINALIZADO(2);

    private final int id;

    EstadoAlquiler(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
