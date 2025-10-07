package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "out_alertas_usuarios")
public class AlertaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="batch", length = 45)
    private String batch;
    @Column(name="nombre_usuario", length = 150)
    private String nomUsuario;
    @Column(name="correo_delta_usuario", length = 150)
    private String correoDelta;
    @Column(name="tipo_usuario")
    private Long tipoUsuario;
    @Column(name="area_usuario")
    private Long areaUsuario;
    @Column(name="estado_registro")
    private Long estadoReg;
    @Column(name = "create_user",length = 20)
    private String createUser;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_user",length = 20)
    private String updateUser;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name="descripcion", length = 255)
    private String descripcion;

    //Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getCorreoDelta() {
        return correoDelta;
    }

    public void setCorreoDelta(String correoDelta) {
        this.correoDelta = correoDelta;
    }

    public Long getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Long tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Long getAreaUsuario() {
        return areaUsuario;
    }

    public void setAreaUsuario(Long areaUsuario) {
        this.areaUsuario = areaUsuario;
    }

    public Long getEstadoReg() {
        return estadoReg;
    }

    public void setEstadoReg(Long estadoReg) {
        this.estadoReg = estadoReg;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
