package com.alp.entities;

public enum TipoRecurso {
    IMAGEN("imagen"), VIDEO("video");
    private final String valorDb;
    TipoRecurso(String valorDb) { this.valorDb = valorDb; }
    public String getValorDb() { return valorDb; }
    public static TipoRecurso desdeString(String t) {
        if ("imagen".equalsIgnoreCase(t)) return IMAGEN;
        if ("video".equalsIgnoreCase(t)) return VIDEO;
        return null;
    }
}