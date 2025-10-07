package com.delta.deltanet.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="rep_folder")
public class AdmFolder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "idpadre")
	private Long idpadre;
	
	@Column(name = "nomfolder")
	private String nomfolder;
	
	@Column(name = "pathfolder")
	private String pathfolder;
	
	@Column(name = "estado")
	private Integer estado;
	
	@Column(name = "create_user")
	private String creuser;
	
	@Column(name = "create_date")
	private Date credate;
	
	@Column(name = "update_user")
	private String upduser;
	
	@Column(name = "update_date")
	private Date upddate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdpadre() {
		return idpadre;
	}

	public void setIdpadre(Long idpadre) {
		this.idpadre = idpadre;
	}

	public String getNomfolder() {
		return nomfolder;
	}

	public void setNomfolder(String nomfolder) {
		this.nomfolder = nomfolder;
	}

	public String getPathfolder() {
		return pathfolder;
	}

	public void setPathfolder(String pathfolder) {
		this.pathfolder = pathfolder;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getCreuser() {
		return creuser;
	}

	public void setCreuser(String creuser) {
		this.creuser = creuser;
	}

	public Date getCredate() {
		return credate;
	}

	public void setCredate(Date credate) {
		this.credate = credate;
	}

	public String getUpduser() {
		return upduser;
	}

	public void setUpduser(String upduser) {
		this.upduser = upduser;
	}

	public Date getUpddate() {
		return upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	
	
	
}
