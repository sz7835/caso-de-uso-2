package com.delta.deltanet.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Entity
@Table(name = "grafo_enlace")
public class Relacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long idRel;

    @Column(name = "id_enlace_motivo",insertable = false,updatable = false)
    private Long idTipoRel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_enlace_motivo")
    private TipoRelacion tipoRel;

    @Column(name = "id_nodo_origen", insertable = false, updatable = false)
    private Long idPersona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_nodo_origen")
    private Persona persona;

    @Column(name = "id_nodo_destino", nullable = true, insertable = false, updatable = false)
    private Long idNodoDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_nodo_destino", nullable = true)
    private Persona personaD;

    @Column(name = "id_reverse", nullable = true)
    private Long idReverse;

    @Column(name = "id_area", nullable = true, insertable = false, updatable = false)
    private Long idArea;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "relacion", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Contrato> contratos;

    @Column(name = "id_enlace_motivo_tipo", nullable = true, insertable = false, updatable = false)
    private Long idNodoTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_enlace_motivo_tipo", nullable = true)
    public NodoTipo nodoTipo;

    @OneToOne
    @JoinColumn(name = "id_area")
    private Areas area;

    @Column(name = "estado")
    private Long estado;

    @Column(name = "fec_inicio")
    @Temporal(TemporalType.DATE)
    private Date fecIni;

    @Column(name = "fec_fin", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date fecFin;
    
    @Column(name = "create_user", length = 20)
    private String createUser;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_user",length = 20)
    private String updateUser;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}

	public Long getIdRel() {
        return idRel;
    }

    public void setIdRel(Long idRel) {
        this.idRel = idRel;
    }

    public Long getIdTipoRel() {
        return idTipoRel;
    }

    public void setIdTipoRel(Long idTipoRel) {
        this.idTipoRel = idTipoRel;
    }

    public TipoRelacion getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(TipoRelacion tipoRel) {
        this.tipoRel = tipoRel;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Long getIdArea() {
        return idArea;
    }

    public void setIdArea(Long idArea) {
        this.idArea = idArea;
    }

    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
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

	public Long getIdNodoDestino() {
		return idNodoDestino;
	}

	public void setIdNodoDestino(Long idNodoDestino) {
		this.idNodoDestino = idNodoDestino;
	}

	public Persona getPersonaD() {
		return personaD;
	}

	public void setPersonaD(Persona personaD) {
		this.personaD = personaD;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getIdReverse() {
		return idReverse;
	}

	public void setIdReverse(Long idReverse) {
		this.idReverse = idReverse;
	}

	public Long getIdNodoTipo() {
		return idNodoTipo;
	}

	public void setIdNodoTipo(Long idNodoTipo) {
		this.idNodoTipo = idNodoTipo;
	}

	public NodoTipo getNodoTipo() {
		return nodoTipo;
	}

	public void setNodoTipo(NodoTipo nodoTipo) {
		this.nodoTipo = nodoTipo;
	}

	@Override
	public String toString() {
		return "Relacion [idRel=" + idRel + ", idTipoRel=" + idTipoRel + ", tipoRel=" + tipoRel + ", idPersona="
				+ idPersona + ", persona=" + persona + ", idNodoDestino=" + idNodoDestino + ", personaD=" + personaD
				+ ", idReverse=" + idReverse + ", idArea=" + idArea + ", contratos=" + contratos + ", idTipoContrato="
				+ idNodoTipo + ", tipoContrato=" + nodoTipo + ", area=" + area + ", estado=" + estado
				+ ", fecIni=" + fecIni + ", fecFin=" + fecFin + ", createUser=" + createUser + ", createDate="
				+ createDate + ", updateUser=" + updateUser + ", updateDate=" + updateDate + "]";
	}
	
    
}
