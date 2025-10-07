package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sec_autorizacion_funcionalidad")
public class Funcionalidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FUNCIONALIDAD")
    private Long iD_FUNCIONALIDAD;

    @Column(name = "ID_FUNCIONALIDAD_PADRE", insertable = false, updatable = false)
    private Long iD_FUNCIONALIDAD_PADRE;

    @OneToOne
    @JoinColumn(name = "ID_FUNCIONALIDAD_PADRE")
    private Funcionalidad father;

    @Column(name = "NOM_FUNCIONALIDAD")
    private String noM_FUNCIONALIDAD;

    @Column(name = "DESC_FUNCIONALIDAD")
    private String desC_FUNCIONALIDAD;

    @Column(name = "SEC_NIVEL")
    private Integer seC_NIVEL;

    @Column(name = "SEC_ORDEN")
    private Integer seC_ORDEN;

    @Column(name = "ID_APLICACION", insertable = false, updatable = false)
    private Integer iD_APLICACION;

    @OneToOne
    @JoinColumn(name="ID_APLICACION")
    private AutorizacionModulo aplication;

    @Column(name = "RUTA")
    private String ruta;

    @Column(name = "ROUTE")
    private String route;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "FLG_MENU")
    private String flG_MENU;

    @Column(name = "FLG_CONTROL")
    private String flG_CONTROL;

    @Column(name = "ESTADO")
    private Integer estado;

    @Column(name = "USR_INGRESO")
    private String usR_INGRESO;

    @Column(name = "FEC_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feC_INGRESO;

    @Column(name = "USR_ULT_MOD")
    private String usR_ULT_MOD;

    @Column(name = "FEC_ULT_MOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feC_ULT_MOD;

    public Long getiD_FUNCIONALIDAD() {
        return iD_FUNCIONALIDAD;
    }

    public void setiD_FUNCIONALIDAD(Long iD_FUNCIONALIDAD) {
        this.iD_FUNCIONALIDAD = iD_FUNCIONALIDAD;
    }

    public Long getiD_FUNCIONALIDAD_PADRE() {
        return iD_FUNCIONALIDAD_PADRE;
    }

    public void setiD_FUNCIONALIDAD_PADRE(Long iD_FUNCIONALIDAD_PADRE) {
        this.iD_FUNCIONALIDAD_PADRE = iD_FUNCIONALIDAD_PADRE;
    }

    public Funcionalidad getFather() {
        return father;
    }

    public void setFather(Funcionalidad father) {
        this.father = father;
    }

    public String getNoM_FUNCIONALIDAD() {
        return noM_FUNCIONALIDAD;
    }

    public void setNoM_FUNCIONALIDAD(String noM_FUNCIONALIDAD) {
        this.noM_FUNCIONALIDAD = noM_FUNCIONALIDAD;
    }

    public String getDesC_FUNCIONALIDAD() {
        return desC_FUNCIONALIDAD;
    }

    public void setDesC_FUNCIONALIDAD(String desC_FUNCIONALIDAD) {
        this.desC_FUNCIONALIDAD = desC_FUNCIONALIDAD;
    }

    public Integer getSeC_NIVEL() {
        return seC_NIVEL;
    }

    public void setSeC_NIVEL(Integer seC_NIVEL) {
        this.seC_NIVEL = seC_NIVEL;
    }

    public Integer getSeC_ORDEN() {
        return seC_ORDEN;
    }

    public void setSeC_ORDEN(Integer seC_ORDEN) {
        this.seC_ORDEN = seC_ORDEN;
    }

    public Integer getiD_APLICACION() {
        return iD_APLICACION;
    }

    public void setiD_APLICACION(Integer iD_APLICACION) {
        this.iD_APLICACION = iD_APLICACION;
    }

    public AutorizacionModulo getAplication() {
        return aplication;
    }

    public void setAplication(AutorizacionModulo aplication) {
        this.aplication = aplication;
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

    public String getFlG_MENU() {
        return flG_MENU;
    }

    public void setFlG_MENU(String flG_MENU) {
        this.flG_MENU = flG_MENU;
    }

    public String getFlG_CONTROL() {
        return flG_CONTROL;
    }

    public void setFlG_CONTROL(String flG_CONTROL) {
        this.flG_CONTROL = flG_CONTROL;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getUsR_INGRESO() {
        return usR_INGRESO;
    }

    public void setUsR_INGRESO(String usR_INGRESO) {
        this.usR_INGRESO = usR_INGRESO;
    }

    public Date getFeC_INGRESO() {
        return feC_INGRESO;
    }

    public void setFeC_INGRESO(Date feC_INGRESO) {
        this.feC_INGRESO = feC_INGRESO;
    }

    public String getUsR_ULT_MOD() {
        return usR_ULT_MOD;
    }

    public void setUsR_ULT_MOD(String usR_ULT_MOD) {
        this.usR_ULT_MOD = usR_ULT_MOD;
    }

    public Date getFeC_ULT_MOD() {
        return feC_ULT_MOD;
    }

    public void setFeC_ULT_MOD(Date feC_ULT_MOD) {
        this.feC_ULT_MOD = feC_ULT_MOD;
    }
}
