package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name="per_persona_tipo")
public class TipoPersona implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long idTipoPer;

    @Column(name="nombre", length = 45)
    public String nombre;

    public Long getIdTipoPer() {
        return idTipoPer;
    }

    public void setIdTipoPer(Long idTipoPer) {
        this.idTipoPer = idTipoPer;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
