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
import javax.persistence.Transient;


@Entity
@Table(name="p_tipo_doc")
public class TipoDoc implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "des_tipo_doc", length = 45, nullable = false)
	private String desTipoDoc;
	
	@Column(name = "cant_caracter", nullable = false)
	private Integer cantCaracter;
	
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


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDesTipoDoc() {
		return desTipoDoc;
	}


	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}


	public Integer getCantCaracter() {
		return cantCaracter;
	}


	public void setCantCaracter(Integer cantCaracter) {
		this.cantCaracter = cantCaracter;
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
		return "TipoDoc [id=" + id + ", desTipoDoc=" + desTipoDoc + ", cantCaracter=" + cantCaracter + ", usuCreado="
				+ usuCreado + ", fechaCreado=" + fechaCreado + ", usuEditado=" + usuEditado + ", fechaEditado="
				+ fechaEditado + ", usuario_sistema=" + usuario_sistema + "]";
	}




	private static final long serialVersionUID = 1L;

}
