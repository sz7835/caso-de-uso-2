package com.delta.deltanet.models.entity;

import java.util.Date;

public class lstContrato {
    private Long idContrato;
    private Long idRelacion;
    private String desTipoRel;
    private String desTipoCto;
    private String desTipoSrv;
    private String desFormPgo;
    private String descripcion;
    private Date fecIni;
    private Date fecFin;
    private Double monto;
    private Long hes;
    private Long estado;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    //Getter and Setter

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(Long idRelacion) {
        this.idRelacion = idRelacion;
    }

    public String getDesTipoRel() {
        return desTipoRel;
    }

    public void setDesTipoRel(String desTipoRel) {
        this.desTipoRel = desTipoRel;
    }

    public String getDesTipoCto() {
        return desTipoCto;
    }

    public void setDesTipoCto(String desTipoCto) {
        this.desTipoCto = desTipoCto;
    }

    public String getDesTipoSrv() {
        return desTipoSrv;
    }

    public void setDesTipoSrv(String desTipoSrv) {
        this.desTipoSrv = desTipoSrv;
    }

    public String getDesFormPgo() {
        return desFormPgo;
    }

    public void setDesFormPgo(String desFormPgo) {
        this.desFormPgo = desFormPgo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Long getHes() {
        return hes;
    }

    public void setHes(Long hes) {
        this.hes = hes;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
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
