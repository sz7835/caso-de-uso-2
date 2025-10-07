package com.delta.deltanet.models.entity;

import java.util.Date;

public class TipoVentaEventoEstadisticasDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer totalSolicitudes;
    private Integer solicitudesAtendidas;
    private Integer solicitudesNoAtendidas;
    private Integer estado;
    private Date fechaSolicitud;

    // Constructor
    public TipoVentaEventoEstadisticasDTO(Integer id, String nombre, String descripcion,
            Long totalSolicitudes, Long solicitudesAtendidas, Long solicitudesNoAtendidas, Integer estado,
            Date fechaSolicitud) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.totalSolicitudes = totalSolicitudes != null ? totalSolicitudes.intValue() : 0;
        this.solicitudesAtendidas = solicitudesAtendidas != null ? solicitudesAtendidas.intValue() : 0;
        this.solicitudesNoAtendidas = solicitudesNoAtendidas != null ? solicitudesNoAtendidas.intValue() : 0;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(Integer totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    public Integer getSolicitudesAtendidas() {
        return solicitudesAtendidas;
    }

    public void setSolicitudesAtendidas(Integer solicitudesAtendidas) {
        this.solicitudesAtendidas = solicitudesAtendidas;
    }

    public Integer getSolicitudesNoAtendidas() {
        return solicitudesNoAtendidas;
    }

    public void setSolicitudesNoAtendidas(Integer solicitudesNoAtendidas) {
        this.solicitudesNoAtendidas = solicitudesNoAtendidas;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}