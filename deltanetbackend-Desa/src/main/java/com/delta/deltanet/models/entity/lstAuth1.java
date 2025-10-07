package com.delta.deltanet.models.entity;

import java.util.List;

public class lstAuth1 {
    private Long id;
    private String usuario;
    private int secPassword;
    private int tipoUsuario;
    private int tipoUsuarioDelta;
    private int codEstUsuario;
    private String nomCompleto;
    private List<lstAuth1Perfil> perfil;
    private List<lstAuth1Func> funcionalidades;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getSecPassword() {
        return secPassword;
    }

    public void setSecPassword(int secPassword) {
        this.secPassword = secPassword;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getTipoUsuarioDelta() {
        return tipoUsuarioDelta;
    }

    public void setTipoUsuarioDelta(int tipoUsuarioDelta) {
        this.tipoUsuarioDelta = tipoUsuarioDelta;
    }

    public int getCodEstUsuario() {
        return codEstUsuario;
    }

    public void setCodEstUsuario(int codEstUsuario) {
        this.codEstUsuario = codEstUsuario;
    }

    public String getNomCompleto() {
        return nomCompleto;
    }

    public void setNomCompleto(String nomCompleto) {
        this.nomCompleto = nomCompleto;
    }

    public List<lstAuth1Perfil> getPerfil() {
        return perfil;
    }

    public void setPerfil(List<lstAuth1Perfil> perfil) {
        this.perfil = perfil;
    }

    public List<lstAuth1Func> getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(List<lstAuth1Func> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }
}
