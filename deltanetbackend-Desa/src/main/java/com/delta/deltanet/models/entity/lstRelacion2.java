package com.delta.deltanet.models.entity;

import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
public class lstRelacion2 {
    private Long idRel;
    private Long idPer;
    private Long idNodoDestino;
    private Long estado;
    private Long idTipoRel;
    private Long idTipoContrato;
    private String idGerencia;
    private String nomGerencia;
    private String idArea;
    private String nomArea;
    private String descrip;
    private Long cantidad;
    private Long tipo;
    private Date fecIni;
    private Date fecFin;
    private List<CatalogoServicio> services;
    private Persona personaD;
    private Persona personaO;

    public Long getIdRel() {
        return idRel;
    }

    public void setIdRel(Long idRel) {
        this.idRel = idRel;
    }

    public Long getIdPer() {
        return idPer;
    }

    public void setIdPer(Long idPer) {
        this.idPer = idPer;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Long getIdTipoRel() {
        return idTipoRel;
    }

    public void setIdTipoRel(Long idTipoRel) {
        this.idTipoRel = idTipoRel;
    }

    public String getIdGerencia() {
        return idGerencia;
    }

    public void setIdGerencia(String idGerencia) {
        this.idGerencia = idGerencia;
    }

    public String getNomGerencia() {
        return nomGerencia;
    }

    public void setNomGerencia(String nomGerencia) {
        this.nomGerencia = nomGerencia;
    }

    public String getNomArea() {
        return nomArea;
    }

    public void setNomArea(String nomArea) {
        this.nomArea = nomArea;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

	public List<CatalogoServicio> getServices() {
		return services;
	}

	public void setServices(List<CatalogoServicio> services) {
		this.services = services;
	}

	public Date getFecFin() {
		return fecFin;
	}

	public void setFecFin(Date fecFin) {
		this.fecFin = fecFin;
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

	public Persona getPersonaO() {
		return personaO;
	}

	public void setPersonaO(Persona personaO) {
		this.personaO = personaO;
	}

	public Long getTipo() {
		return tipo;
	}

	public void setTipo(Long tipo) {
		this.tipo = tipo;
	}

	public Long getIdTipoContrato() {
		return idTipoContrato;
	}

	public void setIdTipoContrato(Long idTipoContrato) {
		this.idTipoContrato = idTipoContrato;
	}
	
}
