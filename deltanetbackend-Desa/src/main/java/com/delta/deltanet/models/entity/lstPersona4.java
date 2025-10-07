package com.delta.deltanet.models.entity;

import java.util.ArrayList;
import java.util.List;

public class lstPersona4 {
    public Long id;
    public List<?> tipoRel = new ArrayList<>();
    public String desArea;
    public String desPuesto;
    public String tipPer;
    public String tipDoc;
    public String nroDoc;
    public String atencion;
    public String nombre;
    public String apePat;
    public String apeMat;
    public String fecNac;
    public String fonoPrin;
    public String fonoPers;
    public String emailPrin;
    public String emailPers;
    public Long idArea;
    public Long idPuesto;
    public List<String> direcciones = new ArrayList<>();
    public String estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public List<String> getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(List<String> tipoRel) {
        this.tipoRel = tipoRel;
    }*/

    public List<?> getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(List<?> tipoRel) {
        this.tipoRel = tipoRel;
    }

    public String getDesArea() {
        return desArea;
    }

    public void setDesArea(String desArea) {
        this.desArea = desArea;
    }

    public String getDesPuesto() {
        return desPuesto;
    }
    public void setDesPuesto(String desPuesto) {
        this.desPuesto = desPuesto;
    }

    public String getTipPer() {
        return tipPer;
    }

    public void setTipPer(String tipPer) {
        this.tipPer = tipPer;
    }

    public String getTipDoc() {
        return tipDoc;
    }

    public void setTipDoc(String tipDoc) {
        this.tipDoc = tipDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getFecNac() {
        return fecNac;
    }

    public void setFecNac(String fecNac) {
        this.fecNac = fecNac;
    }

    public String getFonoPrin() {
        return fonoPrin;
    }

    public void setFonoPrin(String fonoPrin) {
        this.fonoPrin = fonoPrin;
    }

    public String getFonoPers() {
        return fonoPers;
    }

    public void setFonoPers(String fonoPers) {
        this.fonoPers = fonoPers;
    }

    public String getEmailPrin() {
        return emailPrin;
    }

    public void setEmailPrin(String emailPrin) {
        this.emailPrin = emailPrin;
    }

    public String getEmailPers() {
        return emailPers;
    }

    public void setEmailPers(String emailPers) {
        this.emailPers = emailPers;
    }

    public List<String> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<String> direcciones) {
        this.direcciones = direcciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

	public Long getIdArea() {
		return idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public Long getIdPuesto() {
		return idPuesto;
	}

	public void setIdPuesto(Long idPuesto) {
		this.idPuesto = idPuesto;
	}
    
}
