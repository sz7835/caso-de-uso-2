package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "com_puesto_relacion_skill")
public class PuestoOcupacionalRelacionSkill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "puesto_id", nullable = false)
    private Long puestoId;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteSkill skill;

    @Column(name = "skill_id", nullable = false)
    private Long skillId;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPuestoId() {
        return puestoId;
    }

    public void setPuestoId(Long puestoId) {
        this.puestoId = puestoId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public PostulanteSkill getSkill() {
        return skill;
    }

    public void setSkill(PostulanteSkill skill) {
        this.skill = skill;
    }
}
