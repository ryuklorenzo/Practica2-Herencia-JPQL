package com.alp.entities;

public enum OpcionPropiedad {
    VENTA("venta"),
    ALQUILER("alquiler"),
    ALQUILER_OPCION_COMPRA("alquiler con opción a compra"),
    TRASPASO("traspaso");

    private final String valorDb;
    OpcionPropiedad(String valorDb) { this.valorDb = valorDb; }
    public String getValorDb() { return valorDb; }

    public static OpcionPropiedad desdeString(String texto) {
        for (OpcionPropiedad o : values()) {
            if (o.valorDb.equalsIgnoreCase(texto)) return o;
        }
        return null;
    }
}