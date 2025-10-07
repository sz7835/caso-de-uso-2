package com.delta.deltanet.models.entity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class lstPersona2 {
    public Long id;
    public List<String> tipoRel = new ArrayList<>();
    public String desArea;
    public String tipPer;
    public String atencion;
    public String tipDoc;
    public String nroDoc;
    public String nombre;
    public String apePat;
    public String apeMat;
    public String fecNac;
    public List<String> nroTlfs = new ArrayList<>();
    public List<String> correos = new ArrayList<>();
    public List<String> direcciones = new ArrayList<>();
    public Integer estado;
    public String ubigeo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(List<String> tipoRel) {
        this.tipoRel = tipoRel;
    }

    public String getDesArea() {
        return desArea;
    }

    public void setDesArea(String desArea) {
        this.desArea = desArea;
    }

    public String getTipPer() {
        return tipPer;
    }

    public void setTipPer(String tipPer) {
        this.tipPer = tipPer;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
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

    public List<String> getNroTlfs() {
        return nroTlfs;
    }

    public void setNroTlfs(List<String> nroTlfs) {
        this.nroTlfs = nroTlfs;
    }

    public List<String> getCorreos() {
        return correos;
    }

    public void setCorreos(List<String> correos) {
        this.correos = correos;
    }

    public List<String> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<String> direcciones) {
        this.direcciones = direcciones;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }
}
