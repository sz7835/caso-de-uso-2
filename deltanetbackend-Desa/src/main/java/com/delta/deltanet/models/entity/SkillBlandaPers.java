package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name="out_hab_blanda_pers")
public class SkillBlandaPers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="id_persona",insertable = false,updatable = false)
    private Long idPer;

    @OneToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @Column(name="id_hab_blanda",insertable = false,updatable = false)
    private Long idBlanda;

    @OneToOne
    @JoinColumn(name = "id_hab_blanda")
    private SkillBlanda habBlanda;

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

    public Long getIdBlanda() {
        return idBlanda;
    }

    public void setIdBlanda(Long idBlanda) {
        this.idBlanda = idBlanda;
    }

    public SkillBlanda getHabBlanda() {
        return habBlanda;
    }

    public void setHabBlanda(SkillBlanda habBlanda) {
        this.habBlanda = habBlanda;
    }
}
