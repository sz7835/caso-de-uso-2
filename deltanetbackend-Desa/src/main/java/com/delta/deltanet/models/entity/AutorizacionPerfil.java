package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sec_autorizacion_perfil")
public class AutorizacionPerfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERFIL")
    private int id;

    @Column(name = "DSC_NOM_PERFIL")
    private String nombre;

    @Column(name = "COD_EST_PERFIL")
    private int estado;

    @Column(name = "DSC_DESCRIPCION")
    private String descripcion;

    @Column(name = "USR_INGRESO")
    private String usrIngreso;

    @Column(name = "FEC_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIngreso;

    @Column(name = "USR_ULT_MOD")
    private String usrUltMod;

    @Column(name = "FEC_ULT_MOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecUltMod;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsrIngreso() {
        return usrIngreso;
    }

    public void setUsrIngreso(String usrIngreso) {
        this.usrIngreso = usrIngreso;
    }

    public Date getFecIngreso() {
        return fecIngreso;
    }

    public void setFecIngreso(Date fecIngreso) {
        this.fecIngreso = fecIngreso;
    }

    public String getUsrUltMod() {
        return usrUltMod;
    }

    public void setUsrUltMod(String usrUltMod) {
        this.usrUltMod = usrUltMod;
    }

    public Date getFecUltMod() {
        return fecUltMod;
    }

    public void setFecUltMod(Date fecUltMod) {
        this.fecUltMod = fecUltMod;
    }
}
