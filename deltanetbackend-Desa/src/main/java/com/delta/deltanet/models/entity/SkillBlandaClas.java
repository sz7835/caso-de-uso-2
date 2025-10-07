package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="out_hab_blanda_clas")
public class SkillBlandaClas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String descripcion;
    private Integer estado;

    @Column(name = "create_user", length = 50)
    private String createUsr;

    @Column(name="create_date")
    @Temporal(TemporalType.DATE)
    private Date createFec;

    @Column(name = "update_user", length = 50)
    private String updteUsr;

    @Column(name="update_date")
    @Temporal(TemporalType.DATE)
    private Date updateFec;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getCreateUsr() {
        return createUsr;
    }

    public void setCreateUsr(String createUsr) {
        this.createUsr = createUsr;
    }

    public Date getCreateFec() {
        return createFec;
    }

    public void setCreateFec(Date createFec) {
        this.createFec = createFec;
    }

    public String getUpdteUsr() {
        return updteUsr;
    }

    public void setUpdteUsr(String updteUsr) {
        this.updteUsr = updteUsr;
    }

    public Date getUpdateFec() {
        return updateFec;
    }

    public void setUpdateFec(Date updateFec) {
        this.updateFec = updateFec;
    }
}
