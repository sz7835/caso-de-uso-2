package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "per_nat_sexo")
public class Sexo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long idSexo;
    
    @Column(name = "descripcion")
    public String descripcion;
    
	@Column(name = "acronimo")
    public String acronimo;

    @Column(name = "estado")
    public int estado;

    public Long getIdSexo() {
        return idSexo;
    }

    public void setIdSexo(Long idPerfil) {
        this.idSexo = idPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

}
