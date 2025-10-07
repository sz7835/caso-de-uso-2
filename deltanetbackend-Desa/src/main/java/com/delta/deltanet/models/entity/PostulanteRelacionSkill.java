package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "com_postulante_relacion_skill")
public class PostulanteRelacionSkill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "postulante_data_id")
    private Integer postulanteDataId;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteSkill skill;

    @Column(name = "skill_id")
    private Integer skillId;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPostulanteDataId() {
        return postulanteDataId;
    }

    public void setPostulanteDataId(Integer postulanteDataId) {
        this.postulanteDataId = postulanteDataId;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public PostulanteSkill getSkill() {
        return skill;
    }

    public void setSkill(PostulanteSkill skill) {
        this.skill = skill;
    }
}