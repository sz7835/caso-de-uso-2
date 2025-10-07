package com.delta.deltanet.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name = "grafo_enlace_motivo")
public class TipoRelacion implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long idTipoRel;

	@Column(name = "nodo_tipo_origen_id", insertable = false, updatable = false)
	public Long origen;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nodo_tipo_origen_id")
	private NodoTipo nodoOrigen;

	@Column(name = "nodo_tipo_destino_id", insertable = false, updatable = false)
	public Long destino;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nodo_tipo_destino_id")
	private NodoTipo nodoDestino;

	@Column(name = "relacion_tipo_regreso_id")
	public Long relacionback;

	@Column(name = "nombre", length = 45)
	public String descrip;

	@Column(name = "tipo")
	public int tipo;

	@Column(name = "estado")
	public int estado;

	@Column(name = "create_user")
    private String createUser;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

	public Long getIdTipoRel() {
		return idTipoRel;
	}

	public void setIdTipoRel(Long idTipoRel) {
		this.idTipoRel = idTipoRel;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Long getOrigen() {
		return origen;
	}

	public void setOrigen(Long origen) {
		this.origen = origen;
	}

	public Long getDestino() {
		return destino;
	}

	public void setDestino(Long destino) {
		this.destino = destino;
	}

	public Long getRelacionback() {
		return relacionback;
	}

	public void setRelacionback(Long relacionback) {
		this.relacionback = relacionback;
	}

	public NodoTipo getNodoOrigen() {
		return nodoOrigen;
	}

	public void setNodoOrigen(NodoTipo nodoOrigen) {
		this.nodoOrigen = nodoOrigen;
	}

	public NodoTipo getNodoDestino() {
		return nodoDestino;
	}

	public void setNodoDestino(NodoTipo nodoDestino) {
		this.nodoDestino = nodoDestino;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public java.util.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public java.util.Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "TipoRelacion [idTipoRel=" + idTipoRel + ", origen=" + origen + ", nodoOrigen=" + nodoOrigen
				+ ", destino=" + destino + ", nodoDestino=" + nodoDestino + ", relacionback=" + relacionback
				+ ", descrip=" + descrip + ", estado=" + estado + "]";
	}

}
