package com.delta.deltanet.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="sec_autorizacion_funcionalidad")
public class AuthFunc {
    @Id
    @Column(name = "ID_FUNCIONALIDAD")
    private Long idFunc;

    @Column(name = "ID_FUNCIONALIDAD_PADRE")
    private Long idFuncPadre;

    @Column(name = "NOM_FUNCIONALIDAD")
    private String nomFunc ;

    @Column(name = "DESC_FUNCIONALIDAD")
    private String dscFunc ;

    @Column(name = "SEC_NIVEL")
    private int secNivel;

    @Column(name = "SEC_ORDEN")
    private int secOrden;

    @Column(name = "ID_APLICACION")
    private int idApp;

    @Column(name = "RUTA")
    private String ruta;

    @Column(name = "ROUTE")
    private String route;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "FLG_MENU")
    private String flgMenu;

    @Column(name = "FLG_CONTROL")
    private String flgControl;

    @Column(name = "ESTADO")
    private int estado;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authFunc")
    @JsonIgnore
    public List<AuthFuncPerfil> authFncPerfiles;

    public List<AuthFuncPerfil> getAuthFncPerfiles() {
        return authFncPerfiles;
    }

    public void setAuthFncPerfiles(List<AuthFuncPerfil> authFncPerfiles) {
        this.authFncPerfiles = authFncPerfiles;
    }

    public Long getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(Long idFunc) {
        this.idFunc = idFunc;
    }

    public Long getIdFuncPadre() {
        return idFuncPadre;
    }

    public void setIdFuncPadre(Long idFuncPadre) {
        this.idFuncPadre = idFuncPadre;
    }

    public String getNomFunc() {
        return nomFunc;
    }

    public void setNomFunc(String nomFunc) {
        this.nomFunc = nomFunc;
    }

    public String getDscFunc() {
        return dscFunc;
    }

    public void setDscFunc(String dscFunc) {
        this.dscFunc = dscFunc;
    }

    public int getSecNivel() {
        return secNivel;
    }

    public void setSecNivel(int secNivel) {
        this.secNivel = secNivel;
    }

    public int getSecOrden() {
        return secOrden;
    }

    public void setSecOrden(int secOrden) {
        this.secOrden = secOrden;
    }

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFlgMenu() {
        return flgMenu;
    }

    public void setFlgMenu(String flgMenu) {
        this.flgMenu = flgMenu;
    }

    public String getFlgControl() {
        return flgControl;
    }

    public void setFlgControl(String flgControl) {
        this.flgControl = flgControl;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
