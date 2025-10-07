package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name="per_persona_telefono_tipo")
public class TelefonoTipo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JoinColumn(name="id_tipo_persona")
    private Long idTipoPersona;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="estado")
    private int estado;

    @Column(name = "create_user", length = 20, nullable = false)
    private String usuCreado;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreado;

    @Column(name = "update_user", length = 20)
    private String usuUpdate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUpdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdTipoPersona() {
		return idTipoPersona;
	}

	public void setIdTipoPersona(Long idTipoPersona) {
		this.idTipoPersona = idTipoPersona;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public String getUsuUpdate() {
		return usuUpdate;
	}

	public void setUsuUpdate(String usuUpdate) {
		this.usuUpdate = usuUpdate;
	}

	public Date getFechaUpdate() {
		return fechaUpdate;
	}

	public void setFechaUpdate(Date fechaUpdate) {
		this.fechaUpdate = fechaUpdate;
	}


}
