package com.delta.deltanet.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="adm_files")
public class AdmFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nomfile")
	private String name;
	@Column(name = "tipofile")
	private String tipo;
	@Column(name = "tamfile")
	private Long tam;
	@Column(name = "modfile")
	private Date ultmod;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Long getTam() {
		return tam;
	}
	public void setTam(Long tam) {
		this.tam = tam;
	}
	public Date getUltmod() {
		return ultmod;
	}
	public void setUltmod(Date ultmod) {
		this.ultmod = ultmod;
	}
	
	
}
