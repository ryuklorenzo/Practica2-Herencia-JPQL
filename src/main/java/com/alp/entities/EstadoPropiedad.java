package com.alp.entities;

public enum EstadoPropiedad {
    EN_VENTA("en venta"),
    DESACTIVADO("desactivado"),
    VENDIDO("vendido"),
    RESERVADO("reservado");

    private final String valorDb;
    EstadoPropiedad(String valorDb) { this.valorDb = valorDb; }
    public String getValorDb() { return valorDb; }

    public static EstadoPropiedad desdeString(String texto) {
        for (EstadoPropiedad e : values()) {
            if (e.valorDb.equalsIgnoreCase(texto)) return e;
        }
        return null;
    }
}