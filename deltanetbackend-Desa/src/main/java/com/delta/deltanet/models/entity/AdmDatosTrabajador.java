package com.delta.deltanet.models.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "adm_trabajador")
public class AdmDatosTrabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "persona_id", nullable = false)
    private Long personaId;

    @Column(name = "vive_con_quien")
    private String viveConQuien;

    @Column(name = "tiene_hijos_menores", nullable = false)
    private Boolean tieneHijos = false;

    @Column(name = "cantidad_hijos", nullable = false)
    private Integer cantidadHijos = 0;

    @Column(name = "padece_enfermedad", nullable = false)
    private Boolean padeceEnfermedad = false;

    @Column(name = "tiene_discapacidad", nullable = false)
    private Boolean tieneDiscapacidad = false;

    @Column(name = "create_user")
    private String usrCreate;

    @Column(name = "create_date")
    private Date fecCreate;

    @Column(name = "update_user")
    private String usrUpdate;

    @Column(name = "update_date")
    private Date fecUpdate;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public String getViveConQuien() {
        return viveConQuien;
    }

    public void setViveConQuien(String viveConQuien) {
        this.viveConQuien = viveConQuien;
    }

    public Boolean getTieneHijos() {
        return tieneHijos;
    }

    public void setTieneHijos(Boolean tieneHijos) {
        this.tieneHijos = tieneHijos;
    }

    public Integer getCantidadHijos() {
        return cantidadHijos;
    }

    public void setCantidadHijos(Integer cantidadHijos) {
        this.cantidadHijos = cantidadHijos;
    }

    public Boolean getPadeceEnfermedad() {
        return padeceEnfermedad;
    }

    public void setPadeceEnfermedad(Boolean padeceEnfermedad) {
        this.padeceEnfermedad = padeceEnfermedad;
    }

    public Boolean getTieneDiscapacidad() {
        return tieneDiscapacidad;
    }

    public void setTieneDiscapacidad(Boolean tieneDiscapacidad) {
        this.tieneDiscapacidad = tieneDiscapacidad;
    }

    public String getUsrCreate() {
        return usrCreate;
    }

    public void setUsrCreate(String usrCreate) {
        this.usrCreate = usrCreate;
    }

    public Date getFecCreate() {
        return fecCreate;
    }

    public void setFecCreate(Date fecCreate) {
        this.fecCreate = fecCreate;
    }

    public String getUsrUpdate() {
        return usrUpdate;
    }

    public void setUsrUpdate(String usrUpdate) {
        this.usrUpdate = usrUpdate;
    }

    public Date getFecUpdate() {
        return fecUpdate;
    }

    public void setFecUpdate(Date fecUpdate) {
        this.fecUpdate = fecUpdate;
    }
}
