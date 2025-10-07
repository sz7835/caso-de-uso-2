package com.delta.deltanet.models.entity;

import java.util.Date;

public class lstCrono1 {
    private String cliente;
    private String dscCrono;
    //private Long idConsultor;
    //private String nombres;
    //private String apePaterno;
    //private String apeMaterno;
    private String fullname;
    private String username;
    private String tipoDocumento;
    private String nroDocumento;
    private Date periodoInicial;
    private Date periodoFinal;
    private String dias;
    private String vcto;

    //Getter and Setter

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDscCrono() {
        return dscCrono;
    }

    public void setDscCrono(String dscCrono) {
        this.dscCrono = dscCrono;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Date periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public Date getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getVcto() {
        return vcto;
    }

    public void setVcto(String vcto) {
        this.vcto = vcto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }
}
