package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "adm_hijos_trabajador")
public class AdmHijosTrabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trabajador_id", nullable = false)
    private AdmDatosTrabajador trabajador;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "genero_id", nullable = false)
    private Long generoId;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdmDatosTrabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(AdmDatosTrabajador trabajador) {
        this.trabajador = trabajador;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Long getGeneroId() {
        return generoId;
    }

    public void setGeneroId(Long generoId) {
        this.generoId = generoId;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }
}
