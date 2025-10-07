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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="m_personal_interno")
public class PersonalInterno implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = true)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Empresa empresa;
	
	@Column(name = "nombres", length = 100, nullable = false)
	private String nombres;
	
	@Column(name = "ape_paterno", length = 100, nullable = false)
	private String apePaterno;
	
	@Column(name = "ape_materno", length = 100, nullable = false)
	private String apeMaterno;
	
	@Column(name = "atencion", length = 5, nullable = true)
	private String atencion;
	
	@Column(name = "fec_ingreso", nullable = true)
	private Date fecIngreso;
	
	@Column(name = "fec_nacimiento", nullable = false)
	private Date fecNacimiento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_doc", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private TipoDoc tipoDoc;
	
	@Column(name = "nro_doc", length = 20, nullable = false)
	private String nroDoc;
	
	@Column(name = "ruc", length = 11, nullable = true)
	private String ruc;
	
	@Column(name = "direccion", length = 250, nullable = true)
	private String direccion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_ubigeo", referencedColumnName = "id", nullable = true)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Ubigeo ubigeo;
	
	@Column(name = "correo_per", length = 250, nullable = false)
	private String correoPer;
	
	@Column(name = "correo_emp", length = 250, nullable = true)
	private String correoEmp;
	
	@Column(name = "usuario", length = 50, nullable = true)
	private String usuario;
	
	@Column(name = "estado", length = 1, nullable = false)
	private String estado;
	
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApePaterno() {
		return apePaterno;
	}

	public void setApePaterno(String apePaterno) {
		this.apePaterno = apePaterno;
	}

	public String getApeMaterno() {
		return apeMaterno;
	}

	public void setApeMaterno(String apeMaterno) {
		this.apeMaterno = apeMaterno;
	}

	public String getAtencion() {
		return atencion;
	}

	public void setAtencion(String atencion) {
		this.atencion = atencion;
	}

	public Date getFecIngreso() {
		return fecIngreso;
	}

	public void setFecIngreso(Date fecIngreso) {
		this.fecIngreso = fecIngreso;
	}

	public Date getFecNacimiento() {
		return fecNacimiento;
	}

	public void setFecNacimiento(Date fecNacimiento) {
		this.fecNacimiento = fecNacimiento;
	}

	public TipoDoc getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDoc tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getNroDoc() {
		return nroDoc;
	}

	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Ubigeo getUbigeo() {
		return ubigeo;
	}

	public void setUbigeo(Ubigeo ubigeo) {
		this.ubigeo = ubigeo;
	}

	public String getCorreoPer() {
		return correoPer;
	}

	public void setCorreoPer(String correoPer) {
		this.correoPer = correoPer;
	}

	public String getCorreoEmp() {
		return correoEmp;
	}

	public void setCorreoEmp(String correoEmp) {
		this.correoEmp = correoEmp;
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
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
		return "PersonalInterno [id=" + id + ", empresa=" + empresa + ", nombres=" + nombres + ", apePaterno="
				+ apePaterno + ", apeMaterno=" + apeMaterno + ", atencion=" + atencion + ", fecIngreso=" + fecIngreso
				+ ", fecNacimiento=" + fecNacimiento + ", tipoDoc=" + tipoDoc + ", nroDoc=" + nroDoc + ", ruc=" + ruc
				+ ", direccion=" + direccion + ", ubigeo=" + ubigeo + ", correoPer=" + correoPer + ", correoEmp="
				+ correoEmp + ", usuario=" + usuario + ", estado=" + estado + ", usuCreado=" + usuCreado
				+ ", fechaCreado=" + fechaCreado + ", usuEditado=" + usuEditado + ", fechaEditado=" + fechaEditado
				+ "]";
	}


	private static final long serialVersionUID = 1L;
}
