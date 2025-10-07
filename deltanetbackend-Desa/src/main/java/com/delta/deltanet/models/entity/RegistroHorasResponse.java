package com.delta.deltanet.models.entity;

import java.util.Date;

public class RegistroHorasResponse {

    private int id;
    private int idCronograma;
    private int idProyecto;
    private int idPersona;
    private String actividad;
    private int horas;
    private Date dia;
    private int estado;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private String descripcionCronograma;
    private String proyectoDescripcion;
    private String cliente;
    private String supervisor;
    

    // Constructor
    public RegistroHorasResponse(int id, int idCronograma, int idProyecto, int idPersona, String actividad, int horas,
                                Date dia, int estado, String createUser, Date createDate, String updateUser, Date updateDate,
                                String descripcionCronograma, String proyectoDescripcion, String cliente, String supervisor) {
        this.id = id;
        this.idCronograma = idCronograma;
        this.idProyecto = idProyecto;
        this.idPersona = idPersona;
        this.actividad = actividad;
        this.horas = horas;
        this.dia = dia;
        this.estado = estado;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateUser = updateUser;
        this.updateDate = updateDate;
        this.descripcionCronograma = descripcionCronograma;
        this.proyectoDescripcion = proyectoDescripcion;
        this.cliente = cliente;
        this.supervisor = supervisor;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCronograma() {
        return idCronograma;
    }

    public void setIdCronograma(int idCronograma) {
        this.idCronograma = idCronograma;
    }

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

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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

    public String getDescripcionCronograma() {
        return descripcionCronograma;
    }

    public void setDescripcionCronograma(String descripcionCronograma) {
        this.descripcionCronograma = descripcionCronograma;
    }

    public String getProyectoDescripcion() {
        return proyectoDescripcion;
    }

    public void setProyectoDescripcion(String proyectoDescripcion) {
        this.proyectoDescripcion = proyectoDescripcion;
    }

    public String getCliente() {
        return cliente;
    }
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    public String getSupervisor() {
        return supervisor;
    }
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}