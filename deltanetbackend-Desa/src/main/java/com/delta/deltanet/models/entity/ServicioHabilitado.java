package com.delta.deltanet.models.entity;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tkt_servicios_habilitado")
public class ServicioHabilitado implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ServicioHabilitadoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ejecutor_id", insertable = false, updatable = false)
    private ServicioEjecutor ejecutor;

    // Getters y Setters
    public ServicioHabilitadoId getId() {
        return id;
    }

    public void setId(ServicioHabilitadoId id) {
        this.id = id;
    }

    public ServicioEjecutor getEjecutor() {
        return ejecutor;
    }

    public void setEjecutor(ServicioEjecutor ejecutor) {
        this.ejecutor = ejecutor;
    }
}
