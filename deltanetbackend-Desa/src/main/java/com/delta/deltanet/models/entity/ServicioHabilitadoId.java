package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ServicioHabilitadoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "servicio_id")
    private Long servicioId;

    @Column(name = "ejecutor_id")
    private Integer ejecutorId;

    // Constructor por defecto
    public ServicioHabilitadoId() {
    }

    // Constructor con parámetros
    public ServicioHabilitadoId(Long servicioId, Integer ejecutorId) {
        this.servicioId = servicioId;
        this.ejecutorId = ejecutorId;
    }

    // Getters y Setters
    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public Integer getEjecutorId() {
        return ejecutorId;
    }

    public void setEjecutorId(Integer ejecutorId) {
        this.ejecutorId = ejecutorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServicioHabilitadoId))
            return false;
        ServicioHabilitadoId that = (ServicioHabilitadoId) o;
        return Objects.equals(servicioId, that.servicioId) &&
                Objects.equals(ejecutorId, that.ejecutorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(servicioId, ejecutorId);
    }
}
