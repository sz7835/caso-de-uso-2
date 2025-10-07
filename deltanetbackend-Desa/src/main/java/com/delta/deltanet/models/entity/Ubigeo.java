package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name="p_ubigeo")
public class Ubigeo implements Serializable{
	
	@Id
	@Column(name = "id", length = 6)
	private String id;
	
	@Column(name = "dsc_ubigeo", length = 60, nullable = false)
	private String desUbigeo;
	
	@Column(name = "num_departamento", length = 2, nullable = false)
	private String numDepartamento;
	
	@Column(name = "num_provincia", length = 2, nullable = true)
	private String numProvincia;
	
	@Column(name = "num_distrito", length = 2, nullable = true)
	private String numDistrito;
	
	@Column(name = "provincia", length = 4, nullable = true)
	private String provincia;
	
	@Column(name = "usu_creado", length = 50, nullable = false)
	private String usuCreado;
	
	@Column(name = "fecha_creado")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreado;
	
	@Column(name = "usu_editado", length = 50)
	private String usuEditado;
	
	@Column(name = "fecha_editado")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaEditado;
	
	@Transient
	private String usuario_sistema;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesUbigeo() {
		return desUbigeo;
	}

	public void setDesUbigeo(String desUbigeo) {
		this.desUbigeo = desUbigeo;
	}

	public String getNumDepartamento() {
		return numDepartamento;
	}

	public void setNumDepartamento(String numDepartamento) {
		this.numDepartamento = numDepartamento;
	}

	public String getNumProvincia() {
		return numProvincia;
	}

	public void setNumProvincia(String numProvincia) {
		this.numProvincia = numProvincia;
	}

	public String getNumDistrito() {
		return numDistrito;
	}

	public void setNumDistrito(String numDistrito) {
		this.numDistrito = numDistrito;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
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

	public String getUsuario_sistema() {
		return usuario_sistema;
	}

	public void setUsuario_sistema(String usuario_sistema) {
		this.usuario_sistema = usuario_sistema;
	}

	@Override
	public String toString() {
		return "Ubigeo [id=" + id + ", desUbigeo=" + desUbigeo + ", numDepartamento=" + numDepartamento
				+ ", numProvincia=" + numProvincia + ", numDistrito=" + numDistrito + ", provincia=" + provincia
				+ ", usuCreado=" + usuCreado + ", fechaCreado=" + fechaCreado + ", usuEditado=" + usuEditado
				+ ", fechaEditado=" + fechaEditado + ", usuario_sistema=" + usuario_sistema + "]";
	}

	private static final long serialVersionUID = 1L;

}
