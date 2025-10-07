package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name="sec_autorizacion_perfil_usuario")
public class AutorizacionPerfilUsuario {
    @Id
    @Column(name = "ID_USUARIO")
    private Long idUser;

    @Column(name = "ID_PERFIL")
    private int idPerfil;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }
}
