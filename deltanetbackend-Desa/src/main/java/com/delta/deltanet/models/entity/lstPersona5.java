package com.delta.deltanet.models.entity;

import java.util.ArrayList;
import java.util.List;

public class lstPersona5 {
    public Long id;
    public String nombres;
    public String apellidos;
    public String correo;
    public String empresa;
    public String celular;
    public String propietario;
    public String atencion;
    public List<String> tipoRel = new ArrayList<>();
    public String relSel;
    public String motivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
    }

    public List<String> getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(List<String> tipoRel) {
        this.tipoRel = tipoRel;
    }

    public String getRelSel() {
        return relSel;
    }

    public void setRelSel(String relSel) {
        this.relSel = relSel;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
