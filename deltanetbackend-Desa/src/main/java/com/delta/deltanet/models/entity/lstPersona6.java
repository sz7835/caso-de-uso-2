package com.delta.deltanet.models.entity;

import java.util.List;

public class lstPersona6 {
    public Long id;
    public String nombres;
    public String apellidos;
    public List<String> correo;
    public String empresa;
    public String ruc;
    public List<String> celular;
    public String propietario;
    public String atencion;
    public List<String> relacion;
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

    public List<String> getCorreo() {
        return correo;
    }

    public void setCorreo(List<String> correo) {
        this.correo = correo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public List<String> getCelular() {
        return celular;
    }

    public void setCelular(List<String> celular) {
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

    public List<String> getRelacion() {
        return relacion;
    }

    public void setRelacion(List<String> relacion) {
        this.relacion = relacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
