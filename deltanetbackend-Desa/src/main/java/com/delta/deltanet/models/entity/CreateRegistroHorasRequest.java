package com.delta.deltanet.models.entity;

import java.util.List;

public class CreateRegistroHorasRequest {
    // private int idCronograma;
    private int idProyecto;
    private int idPersona;
    private List<DetalleRegistro> detalle;
    private String dia;
    private String createUser;

    // Getters and setters
    // public int getIdCronograma() {
    //     return idCronograma;
    // }

    // public void setIdCronograma(int idCronograma) {
    //     this.idCronograma = idCronograma;
    // }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public List<DetalleRegistro> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleRegistro> detalle) {
        this.detalle = detalle;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}

