package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="per_persona_cliente")
public class PersonaCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="id_persona",insertable = false,updatable = false)
    private Long idPersona;

    @Column(name="id_cliente",insertable = false,updatable = false)
    private Long idCliente;

    @OneToOne
    @JoinColumn(name = "id_persona")
    private Persona person;

    @OneToOne
    @JoinColumn(name = "id_cliente")
    private Persona client;

    private Long estado;

    @Column(name = "create_user", length = 50)
    private String createUsr;

    @Column(name="create_date")
    @Temporal(TemporalType.DATE)
    private Date createFec;

    @Column(name = "update_user", length = 50)
    private String updteUsr;

    @Column(name="update_date")
    @Temporal(TemporalType.DATE)
    private Date updateFec;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Persona getPerson() {
		return person;
	}

	public void setPerson(Persona person) {
		this.person = person;
	}

	public Persona getClient() {
		return client;
	}

	public void setClient(Persona client) {
		this.client = client;
	}

	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
		this.estado = estado;
	}

	public String getCreateUsr() {
		return createUsr;
	}

	public void setCreateUsr(String createUsr) {
		this.createUsr = createUsr;
	}

	public Date getCreateFec() {
		return createFec;
	}

	public void setCreateFec(Date createFec) {
		this.createFec = createFec;
	}

	public String getUpdteUsr() {
		return updteUsr;
	}

	public void setUpdteUsr(String updteUsr) {
		this.updteUsr = updteUsr;
	}

	public Date getUpdateFec() {
		return updateFec;
	}

	public void setUpdateFec(Date updateFec) {
		this.updateFec = updateFec;
	}

}
