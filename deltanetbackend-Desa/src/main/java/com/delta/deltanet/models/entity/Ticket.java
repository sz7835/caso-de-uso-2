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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
//@Table(name="ticket")
@Table(name="tkt_ticket_principal")
public class Ticket implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_destino_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private OrgAreas areaDestino;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_origen_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private OrgAreas areaOrigen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "catalogo_servicio_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CatalogoServicio catalogoServicio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Categoria categoria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prioridad_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Prioridad prioridad;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_servicio_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private AutenticacionUsuario autenticacionUsuario;

	@Column(name = "usuario_creador", length = 50, nullable = false)
	private String usuarioCreador;

	@Column(name = "tipo_usuario_creador", length = 1, nullable = false)
	private String tipoUsuarioCreador;

	@Column(name = "titulo", length = 250, nullable = false, columnDefinition = "text")
	@Lob
	private String titulo;

	@Column(name = "descripcion", nullable = false, columnDefinition = "text")
	@Lob
	private String descripcion;

	@Column(name = "usu_creado", length = 50, nullable = false)
	private String usuCreado;

	@Column(name = "fecha_creado", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreado;

	@Column(name = "usu_editado", length = 50)
	private String usuEditado;

	@Column(name = "fecha_editado")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaEditado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Estado estado;

	@PrePersist
	public void prePersist() {
		//fechaCreado = new Date();
		tipoUsuarioCreador = "I";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrgAreas getAreaDestino() {
		return areaDestino;
	}

	public void setAreaDestino(OrgAreas areaDestino) {
		this.areaDestino = areaDestino;
	}

	public OrgAreas getAreaOrigen() {
		return areaOrigen;
	}

	public void setAreaOrigen(OrgAreas areaOrigen) {
		this.areaOrigen = areaOrigen;
	}

	public CatalogoServicio getCatalogoServicio() {
		return catalogoServicio;
	}

	public void setCatalogoServicio(CatalogoServicio catalogoServicio) {
		this.catalogoServicio = catalogoServicio;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Prioridad getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}

	public AutenticacionUsuario getAutenticacionUsuario() {
		return autenticacionUsuario;
	}

	public void setAutenticacionUsuario(AutenticacionUsuario usuarioServicio) {
		this.autenticacionUsuario = usuarioServicio;
	}

	public String getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}

	public String getTipoUsuarioCreador() {
		return tipoUsuarioCreador;
	}

	public void setTipoUsuarioCreador(String tipoUsuarioCreador) {
		this.tipoUsuarioCreador = tipoUsuarioCreador;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	private static final long serialVersionUID = 1L;
}
