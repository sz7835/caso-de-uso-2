package com.delta.deltanet.models.entity;

import java.time.LocalDate;

public class BusquedaPropuestaDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer monedaId;
    private Integer estadoId;

    // Constructores
    public BusquedaPropuestaDTO() {
    }

    public BusquedaPropuestaDTO(LocalDate fechaInicio, LocalDate fechaFin, Integer monedaId, Integer estadoId) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monedaId = monedaId;
        this.estadoId = estadoId;
    }

    // Getters y Setters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getMonedaId() {
        return monedaId;
    }

    public void setMonedaId(Integer monedaId) {
        this.monedaId = monedaId;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }
}