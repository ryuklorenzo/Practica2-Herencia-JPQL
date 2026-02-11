package com.alp.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "propiedades")
public class PropiedadesJPA {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "referencia", nullable = false, length = 20)
    private String referencia;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id")
    private LocalidadesJPA localidad;

    @Column(name = "metros_cuadrados", nullable = false, precision = 10, scale = 2)
    private BigDecimal metrosCuadrados;

    @Column(name = "coordenadas", columnDefinition = "point")
    private String coordenadas;

    @Column(name = "precio", nullable = false, precision = 15, scale = 2)
    private BigDecimal precio;

    @Column(name = "precio_rebajado", precision = 15, scale = 2)
    private BigDecimal precioRebajado;

    @ColumnDefault("'en venta'")
    @Column(name = "estado", columnDefinition = "estado_propiedad")
    private String estado;

    @Column(name = "opcion", columnDefinition = "opcion_propiedad not null")
    private String opcion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @OneToOne(mappedBy = "propiedades")
    private LocalesJPA localesJPA;

    @OneToMany
    private Set<MultimediaJPA> multimedia = new LinkedHashSet<>();

    @OneToOne(mappedBy = "propiedades")
    private TerrenoJPA terreno;

    @OneToOne(mappedBy = "propiedades")
    private ViviendaJPA vivienda;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalidadesJPA getLocalidad() {
        return localidad;
    }

    public void setLocalidad(LocalidadesJPA localidad) {
        this.localidad = localidad;
    }

    public BigDecimal getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(BigDecimal metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioRebajado() {
        return precioRebajado;
    }

    public void setPrecioRebajado(BigDecimal precioRebajado) {
        this.precioRebajado = precioRebajado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalesJPA getLocale() {
        return localesJPA;
    }

    public void setLocale(LocalesJPA localesJPA) {
        this.localesJPA = localesJPA;
    }

    public Set<MultimediaJPA> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Set<MultimediaJPA> multimedia) {
        this.multimedia = multimedia;
    }

    public TerrenoJPA getTerreno() {
        return terreno;
    }

    public void setTerreno(TerrenoJPA terreno) {
        this.terreno = terreno;
    }

    public ViviendaJPA getVivienda() {
        return vivienda;
    }

    public void setVivienda(ViviendaJPA vivienda) {
        this.vivienda = vivienda;
    }

}