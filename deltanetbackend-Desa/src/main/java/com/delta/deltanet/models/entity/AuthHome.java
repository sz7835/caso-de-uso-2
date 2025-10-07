package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sec_autenticacion_page_default")
public class AuthHome {
    @Id
    @Column(name = "id_user_auth")
    private Long id;

    @Column(name = "id_funcionalidad")
    private Long idFunc;

    private String ruta;
    private String route;

    @Column(name = "usr_ingreso")
    private String usrIngreso;

    @Column(name = "fec_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIngreso;

    @Column(name = "usr_ult_mod")
    private String usrUltMod;

    @Column(name = "fec_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecUltMod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(Long idFunc) {
        this.idFunc = idFunc;
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
