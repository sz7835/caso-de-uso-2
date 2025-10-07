package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ubi_provincia")
public class Provincia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long id;

    @Column(name="nombre", length = 100)
    public String descripcion;

    @ManyToOne
    @JoinColumn(name="id_departamento")
    public Departamento dpto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Departamento getDepartamento() {
        return dpto;
    }

    public void setDepartamento(Departamento dpto) {
        this.dpto = dpto;
    }
}
