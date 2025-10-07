package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name="per_persona_telefono")
public class Telefono implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long idTelefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_persona")
    private Persona persona;

    @Column(name="id_tipo")
    private Long tipo;

    @Column(name="numero",length = 15)
    private String numero;

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


    @PrePersist
    public void prePersist() {
        estado = 1;
    }

    public Long getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(Long idTelefono) {
        this.idTelefono = idTelefono;
    }


    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
