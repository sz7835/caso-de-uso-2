package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "com_postulante_relacion_requisitos")
public class PostulanteRelacionRequisitos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "postulante_data_id")
    private Integer postulanteDataId;

    @ManyToOne
    @JoinColumn(name = "requisito_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteRequisito requisito;

    @Column(name = "requisito_id")
    private Integer requisitoId;

    @Column(name = "nivel_id")
    private Integer nivelId;

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

    public Integer getRequisitoId() {
        return requisitoId;
    }

    public void setRequisitoId(Integer requisitoId) {
        this.requisitoId = requisitoId;
    }

    public Integer getNivelId() {
        return nivelId;
    }

    public void setNivelId(Integer nivelId) {
        this.nivelId = nivelId;
    }

    public PostulanteRequisito getRequisito() {
        return requisito;
    }

    public void setRequisito(PostulanteRequisito requisito) {
        this.requisito = requisito;
    }
}
