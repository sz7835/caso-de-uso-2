package com.delta.deltanet.models.entity;

import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
public class lstPersonaJuridica {
    public Long id;
    public String tipPer;
    public String tipDoc;
    public String nroDoc;
    public String tratamiento;
    public String razSocial;
    public String razComercial;
    public String fechaInicio;
    public String idTitular;
    public String nombreTitular;
    public List<lstTelefono1> telefonos;
    public List<lstEmail1> correos;
    public List<Relacion> relaciones;
    public List<Direccion> direcciones;
    public String estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getRazSocial() {
        return razSocial;
    }

    public void setRazSocial(String razSocial) {
        this.razSocial = razSocial;
    }

    public String getRazComercial() {
        return razComercial;
    }

    public void setRazComercial(String razComercial) {
        this.razComercial = razComercial;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setIdTitular(String idTitular) {
        this.idTitular = idTitular;
    }

    public void setIdTitular(Integer idTitular) {
        this.idTitular = idTitular.toString();
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public List<lstTelefono1> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<lstTelefono1> telefonos) {
        this.telefonos = telefonos;
    }

    public List<lstEmail1> getCorreos() {
        return correos;
    }

    public void setCorreos(List<lstEmail1> correos) {
        this.correos = correos;
    }

    public List<Relacion> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(List<Relacion> relaciones) {
        this.relaciones = relaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }
}