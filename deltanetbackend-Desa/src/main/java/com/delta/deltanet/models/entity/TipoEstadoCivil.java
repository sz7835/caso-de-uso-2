package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "per_nat_estado_civil")
public class TipoEstadoCivil implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Long estado;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
		this.estado = estado;
	}
    
}