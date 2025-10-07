package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.FetchType;

@Entity
@Table(name="tkt_servicio_usuario")
public class UsuarioServicio implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	//many to many con catalogoServicio
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tkt_servicios_habilitado",
        joinColumns = @JoinColumn(name = "ejecutor_id"),
        inverseJoinColumns = @JoinColumn(name = "servicio_id"),
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"ejecutor_id", "servicio_id"})
        }
    )
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "usuarios" })
    private List<CatalogoServicio> catalogoServicios;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id", referencedColumnName = "id", nullable = true)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Area area;

	@Column(name = "usuario", length = 50, nullable = false)
	private String usuario;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;
	
	@Column(name = "apellidos", length = 100, nullable = false)
	private String apellidos;

	@Column(name = "correo_electronico", length = 100, nullable = false)
	private String correoElectronico;
	
	@Column(name = "rol", nullable = false)
	private char rol;
	
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
	
	@Column(name = "estado_registro")
	private String estadoRegistro;
	
	@PrePersist
	public void prePersist() {
		fechaCreado = new Date();
		//rol = 2;
		estadoRegistro = "A";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}


	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String email) {
		this.correoElectronico = email;
	}

	public char getRol() {
		return rol;
	}

	public void setRol(char rol) {
		this.rol = rol;
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

	public List<CatalogoServicio> getCatalogoServicios() {
		return catalogoServicios;
	}

	public void setCatalogoServicios(List<CatalogoServicio> catalogoServicios) {
		this.catalogoServicios = catalogoServicios;
	}



	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	@Override
	public String toString() {
		return "UsuarioServicio [id=" + id + ", catalogoServicios=" + catalogoServicios + ", area=" + area
				+ ", usuario=" + usuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", correoElectronico=" + correoElectronico +", rol=" + rol
				+ ", usuCreado=" + usuCreado + ", fechaCreado=" + fechaCreado + ", usuEditado=" + usuEditado
				+ ", fechaEditado=" + fechaEditado + ", estadoRegistro=" + estadoRegistro + "]";
	}



	private static final long serialVersionUID = 1L;

}
