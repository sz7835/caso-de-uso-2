package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "com_puesto_relacion_requisito")
public class PuestoOcupacionalRelacionRequisito implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "puesto_id", nullable = false)
    private Long puestoId;

    @ManyToOne
    @JoinColumn(name = "requisito_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteRequisito requisito;

    @Column(name = "requisito_id", nullable = false)
    private Long requisitoId;

    @Column(name = "nivel_id", nullable = false)
    private Long nivelId;

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

    public Long getRequisitoId() {
        return requisitoId;
    }

    public void setRequisitoId(Long requisitoId) {
        this.requisitoId = requisitoId;
    }

    public Long getNivelId() {
        return nivelId;
    }

    public void setNivelId(Long nivelId) {
        this.nivelId = nivelId;
    }

    public PostulanteRequisito getRequisito() {
        return requisito;
    }

    public void setRequisito(PostulanteRequisito requisito) {
        this.requisito = requisito;
    }
}
