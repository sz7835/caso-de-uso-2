package com.delta.deltanet.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Entity
@Table(name="per_contacto")
public class Contacto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long id;

    @Column(name = "id_tipo")
    public Long idTipo;

    @OneToOne
    @JoinColumn(name="persona")
    public Persona persona;

    @OneToOne
    @JoinColumn(name="contacto")
    public Persona contacto;
/*
    @OneToOne
    @JoinColumn(name = "motivo")
    public MotivoContacto motivo;*/

    @Column(name = "motivo", insertable = false,updatable = false)
    public Long idMotivo;

    @Column(name = "estado")
    public Long estado;

    @Column(name = "create_user", length = 20)
    public String createUser;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date createDate;

    @Column(name = "update_user", length = 20)
    public String updateUser;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date updateDate;

    public Long getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Persona getContacto() {
        return contacto;
    }

    public void setContacto(Persona contacto) {
        this.contacto = contacto;
    }
/*
    public MotivoContacto getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoContacto motivo) {
        this.motivo = motivo;
    }*/

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(Long idMotivo) {
        this.idMotivo = idMotivo;
    }
}
