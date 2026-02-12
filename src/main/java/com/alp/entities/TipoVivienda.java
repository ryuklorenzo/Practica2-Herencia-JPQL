package com.alp.entities;

public enum TipoVivienda {
    PISO("piso"),
    CHALET("chalet"),
    ADOSADO("adosado");

    private final String valorDb;

    TipoVivienda(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    public static TipoVivienda desdeString(String texto) {
        for (TipoVivienda tipo : TipoVivienda.values()) {
            if (tipo.valorDb.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de vivienda no válido: " + texto);
    }
}