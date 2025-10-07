package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name="out_hab_dura_tecno_persona")
public class SkillDuraPers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="id_persona",insertable = false,updatable = false)
    private Long idPer;

    @OneToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @Column(name="id_hab_dura",insertable = false,updatable = false)
    private Long idDura;

    @OneToOne
    @JoinColumn(name = "id_hab_dura")
    private SkillDura habDura;

    @Column(name="id_hab_dura_framework",insertable = false,updatable = false)
    private Long idDuraFw;

    @OneToOne
    @JoinColumn(name = "id_hab_dura_framework")
    private SkillDuraFrameWork habDuraFw;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPer() {
        return idPer;
    }

    public void setIdPer(Long idPer) {
        this.idPer = idPer;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Long getIdDura() {
        return idDura;
    }

    public void setIdDura(Long idDura) {
        this.idDura = idDura;
    }

    public SkillDura getHabDura() {
        return habDura;
    }

    public void setHabDura(SkillDura habDura) {
        this.habDura = habDura;
    }

    public Long getIdDuraFw() {
        return idDuraFw;
    }

    public void setIdDuraFw(Long idDuraFw) {
        this.idDuraFw = idDuraFw;
    }

    public SkillDuraFrameWork getHabDuraFw() {
        return habDuraFw;
    }

    public void setHabDuraFw(SkillDuraFrameWork habDuraFw) {
        this.habDuraFw = habDuraFw;
    }
}
