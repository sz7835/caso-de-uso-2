package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "adm_contrato_marco_perfiles")
public class ContratoPerfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idContratoPerfil;

    @Column(name = "contrato_id")
    private Long idContrato;

    @Column(name = "perfil_id")
    private Long idPerfil;

    public Long getIdContratoPerfil() {
        return idContratoPerfil;
    }

    public void setIdContratoPerfil(Long idContratoPerfil) {
        this.idContratoPerfil = idContratoPerfil;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }
}
