package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="m_historial_outsourcing")
public class HistorialOutsourcing implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_personal", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private PersonalInterno personal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Empresa empresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cargo", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Cargo cargo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_jefe_ext", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private PersonalExterno jefeExt;
	
	@Column(name = "fec_inicio_ope", nullable = false)
	private Date fecInicioOpe;
	
	@Column(name = "fec_fin_ope", nullable = true)
	private Date fecFinOpe;
	
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

	public PersonalInterno getPersonal() {
		return personal;
	}

	public void setPersonal(PersonalInterno personal) {
		this.personal = personal;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public PersonalExterno getJefeExt() {
		return jefeExt;
	}

	public void setJefeExt(PersonalExterno jefeExt) {
		this.jefeExt = jefeExt;
	}

	public Date getFecInicioOpe() {
		return fecInicioOpe;
	}

	public void setFecInicioOpe(Date fecInicioOpe) {
		this.fecInicioOpe = fecInicioOpe;
	}

	public Date getFecFinOpe() {
		return fecFinOpe;
	}

	public void setFecFinOpe(Date fecFinOpe) {
		this.fecFinOpe = fecFinOpe;
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
		return "HistorialOutsourcing [id=" + id + ", personal=" + personal + ", empresa=" + empresa + ", cargo=" + cargo
				+ ", jefeExt=" + jefeExt + ", fecInicioOpe=" + fecInicioOpe + ", fecFinOpe=" + fecFinOpe
				+ ", usuCreado=" + usuCreado + ", fechaCreado=" + fechaCreado + ", usuEditado=" + usuEditado
				+ ", fechaEditado=" + fechaEditado + ", usuario_sistema=" + usuario_sistema + "]";
	}



	private static final long serialVersionUID = 1L;

}
