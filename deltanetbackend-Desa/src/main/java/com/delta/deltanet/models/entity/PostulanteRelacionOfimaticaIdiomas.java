package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "com_postulante_relacion_ofimatica_idiomas")
public class PostulanteRelacionOfimaticaIdiomas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "postulante_data_id")
    private Integer postulanteDataId;

    @ManyToOne
    @JoinColumn(name = "ofimatica_idiomas_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteOfimaticaIdiomas ofimaticaIdiomas;

    @Column(name = "ofimatica_idiomas_id")
    private Integer ofimaticaIdiomasId;

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

    public PostulanteOfimaticaIdiomas getOfimaticaIdiomas() {
        return ofimaticaIdiomas;
    }

    public void setOfimaticaIdiomas(PostulanteOfimaticaIdiomas ofimaticaIdiomas) {
        this.ofimaticaIdiomas = ofimaticaIdiomas;
    }

    public Integer getOfimaticaIdiomasId() {
        return ofimaticaIdiomasId;
    }

    public void setOfimaticaIdiomasId(Integer ofimaticaIdiomasId) {
        this.ofimaticaIdiomasId = ofimaticaIdiomasId;
    }

    public Integer getNivelId() {
        return nivelId;
    }

    public void setNivelId(Integer nivelId) {
        this.nivelId = nivelId;
    }
}
