package com.delta.deltanet.models.entity;

import java.util.Date;

@SuppressWarnings("all")
public class lstEmail1 {
    private Long idEMail;
    private Long idPersona;
    private Long idContacto;
    private Long tipo;
    private String correo;
    private int estado;
    private String usuCreado;
    private Date fechaCreado;
    private String usuUpdate;
    private Date fechaUpdate;

    public Long getIdEMail() {
        return idEMail;
    }

    public void setIdEMail(Long idEMail) {
        this.idEMail = idEMail;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Long getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Long idContacto) {
        this.idContacto = idContacto;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getUsuCreado() {
        return usuCreado;
    }

    public void setUsuCreado(String usuCreado) {
        this.usuCreado = usuCreado;
    }

    public Date getFechaCreado() {
        return fechaCreado;
    }

    public void setFechaCreado(Date fechaCreado) {
        this.fechaCreado = fechaCreado;
    }

    public String getUsuUpdate() {
        return usuUpdate;
    }

    public void setUsuUpdate(String usuUpdate) {
        this.usuUpdate = usuUpdate;
    }

    public Date getFechaUpdate() {
        return fechaUpdate;
    }

    public void setFechaUpdate(Date fechaUpdate) {
        this.fechaUpdate = fechaUpdate;
    }
}
