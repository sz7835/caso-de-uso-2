package com.delta.deltanet.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="srv_informe")
public class ServicioInforme {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@OneToOne
	@JoinColumn(name="id_cli")
	private PersonaJuridica cliente;
	
	@Column(name = "numero")
	private Integer numero;
	
	@Column(name = "total_horas")
	private double totalHoras;
	
	@Column(name = "mes_fac")
	private String mesFac;
	
	@Column(name = "anio_fac")
	private String yearFac;
	
	@Column(name = "list_tickets")
	private String listTickets;
	
	@Column(name = "create_user")
    private String createUser;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_date")
    private Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PersonaJuridica getCliente() {
		return cliente;
	}

	public void setCliente(PersonaJuridica cliente) {
		this.cliente = cliente;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public double getTotalHoras() {
		return totalHoras;
	}

	public void setTotalHoras(double totalHoras) {
		this.totalHoras = totalHoras;
	}

	public String getMesFac() {
		return mesFac;
	}

	public void setMesFac(String mesFac) {
		this.mesFac = mesFac;
	}

	public String getYearFac() {
		return yearFac;
	}

	public void setYearFac(String yearFac) {
		this.yearFac = yearFac;
	}

	public String getListTickets() {
		return listTickets;
	}

	public void setListTickets(String listTickets) {
		this.listTickets = listTickets;
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

    
}
