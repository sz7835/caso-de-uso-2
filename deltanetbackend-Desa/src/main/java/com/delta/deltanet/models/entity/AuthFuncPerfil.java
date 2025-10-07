package com.delta.deltanet.models.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="sec_autorizacion_funcionalidad_perfil")
public class AuthFuncPerfil {
    @Id
    @Column(name = "ID_PERFIL")
    private int idPerfil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="ID_FUNCIONALIDAD",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public AuthFunc authFunc;

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public AuthFunc getAuthFunc() {
        return authFunc;
    }

    public void setAuthFunc(AuthFunc authFunc) {
        this.authFunc = authFunc;
    }
}
