package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="adm_contrato_modalidad")
public class Modalidad implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nombre", length = 45, nullable = false)
	private String nombre;
	
	@Column(name = "estado", nullable = false)
	private int estado;
	
	@Column(name = "create_user", length = 45, nullable = false)
	private String usuCreado;
	
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreado;
	
	@Column(name = "update_user", length = 45)
	private String usuEditado;
	
	@Column(name = "update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaEditado;

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



	public int getEstado() {
		return estado;
	}



	public void setEstado(int estado) {
		this.estado = estado;
	}



	public String getUsuCreado() {
		return usuCreado;
	}



	public void setUsuCreado(String usuCreado) {
		this.usuCreado = usuCreado;
	}



	public Date getFechaCreado() {
		return fechaCreado;
	}



	public void setFechaCreado(Date fechaCreado) {
		this.fechaCreado = fechaCreado;
	}



	public String getUsuEditado() {
		return usuEditado;
	}



	public void setUsuEditado(String usuEditado) {
		this.usuEditado = usuEditado;
	}



	public Date getFechaEditado() {
		return fechaEditado;
	}



	public void setFechaEditado(Date fechaEditado) {
		this.fechaEditado = fechaEditado;
	}

	@Override
	public String toString() {
		return "Modalidad [id=" + id + ", nombre=" + nombre + ", estado=" + estado + ", usuCreado=" + usuCreado
				+ ", fechaCreado=" + fechaCreado + ", usuEditado=" + usuEditado + ", fechaEditado=" + fechaEditado
				+ "]";
	}
	
	public ModalidadDto toDto() {
		return new ModalidadDto(this.getId(), this.getNombre());
	}



	private static final long serialVersionUID = 1L;
}
