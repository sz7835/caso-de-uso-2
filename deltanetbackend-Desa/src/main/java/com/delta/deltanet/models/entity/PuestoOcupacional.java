package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name = "com_puesto")
public class PuestoOcupacional implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "unidad_organica", length = 100, nullable = false)
    private String unidadOrganica;

    @Column(name = "nombre_puesto", length = 255, nullable = false)
    private String nombrePuesto;

    @Column(name = "dependencia_jerarquica_funcional", length = 255, nullable = false)
    private String dependenciaJerarquicaFuncional;

    @Column(name = "puesto_que_supervisa", length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'No aplica'")
    private String puestoQueSupervisa;

    @Column(name = "mision_del_puesto", columnDefinition = "TEXT", nullable = false)
    private String misionDelPuesto;

    @Column(name = "funciones_del_puesto", columnDefinition = "TEXT", nullable = false)
    private String funcionesDelPuesto;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "create_user", length = 50, nullable = false)
    private String createUser;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user", length = 50)
    private String updateUser;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnidadOrganica() {
        return unidadOrganica;
    }

    public void setUnidadOrganica(String unidadOrganica) {
        this.unidadOrganica = unidadOrganica;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getDependenciaJerarquicaFuncional() {
        return dependenciaJerarquicaFuncional;
    }

    public void setDependenciaJerarquicaFuncional(String dependenciaJerarquicaFuncional) {
        this.dependenciaJerarquicaFuncional = dependenciaJerarquicaFuncional;
    }

    public String getPuestoQueSupervisa() {
        return puestoQueSupervisa;
    }

    public void setPuestoQueSupervisa(String puestoQueSupervisa) {
        this.puestoQueSupervisa = puestoQueSupervisa;
    }

    public String getMisionDelPuesto() {
        return misionDelPuesto;
    }

    public void setMisionDelPuesto(String misionDelPuesto) {
        this.misionDelPuesto = misionDelPuesto;
    }

    public String getFuncionesDelPuesto() {
        return funcionesDelPuesto;
    }

    public void setFuncionesDelPuesto(String funcionesDelPuesto) {
        this.funcionesDelPuesto = funcionesDelPuesto;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
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
}
