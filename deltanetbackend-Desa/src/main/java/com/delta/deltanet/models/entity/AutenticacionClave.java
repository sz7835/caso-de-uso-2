package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="sec_autenticacion_clave")
public class AutenticacionClave implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "SEC_PASSWORD")
    private int secPassword;

    @Column(name = "DSC_PASSWORD")
    private String desPassword;

    @Column(name = "USR_INGRESO")
    private String usrIngreso;

    @Column(name = "FEC_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIngreso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSecPassword() {
        return secPassword;
    }

    public void setSecPassword(int secPassword) {
        this.secPassword = secPassword;
    }

    public String getDesPassword() {
        return desPassword;
    }

    public void setDesPassword(String desPassword) {
        this.desPassword = desPassword;
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
}
