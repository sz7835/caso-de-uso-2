package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name = "com_vacante_relacion_ofimatica_idiomas")
public class VacanteRelacionOfimaticaIdiomas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vacante_id", nullable = false)
    private int vacanteId;

    @ManyToOne
    @JoinColumn(name = "ofimatica_idiomas_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteOfimaticaIdiomas ofimaticaIdiomas;

    @Column(name = "ofimatica_idiomas_id", nullable = false)
    private int ofimaticaIdiomasId;

    @Column(name = "nivel_id", nullable = false)
    private int nivelId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVacanteId() {
        return vacanteId;
    }

    public void setVacanteId(int vacanteId) {
        this.vacanteId = vacanteId;
    }

    public int getOfimaticaIdiomasId() {
        return ofimaticaIdiomasId;
    }

    public void setOfimaticaIdiomasId(int ofimaticaIdiomasId) {
        this.ofimaticaIdiomasId = ofimaticaIdiomasId;
    }

    public int getNivelId() {
        return nivelId;
    }

    public void setNivelId(int nivelId) {
        this.nivelId = nivelId;
    }

    public PostulanteOfimaticaIdiomas getOfimaticaIdiomas() {
        return ofimaticaIdiomas;
    }

    public void setOfimaticaIdiomas(PostulanteOfimaticaIdiomas ofimaticaIdiomas) {
        this.ofimaticaIdiomas = ofimaticaIdiomas;
    }
}