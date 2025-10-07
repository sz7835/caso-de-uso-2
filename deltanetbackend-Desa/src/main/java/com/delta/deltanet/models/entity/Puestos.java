package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "adm_org_jefaturas_puestos")
public class Puestos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_area", nullable = true, insertable = false, updatable = false)
	private Long idArea;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_area")
	@JsonBackReference
	public OrgAreas area;

	@Column(name = "nombre", length = 100)
	private String nombre;

	@Column(name = "estado")
	private Integer estado;

	@Column(name = "create_user", length = 20, updatable = false)
	private String createUser;

	@Column(name = "create_date", updatable = false)
	private LocalDateTime createDate;

	@Column(name = "update_user", length = 20)
	private String updateUser;

	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@PrePersist
	public void prePersist() {
		this.createDate = LocalDateTime.now();
		this.estado = 1;
	}

	@PreUpdate
	public void preUpdate() {
		this.updateDate = LocalDateTime.now();
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public Long getIdArea() {
		return idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public OrgAreas getArea() {
		return area;
	}

	public void setArea(OrgAreas area) {
		this.area = area;
	}

}
