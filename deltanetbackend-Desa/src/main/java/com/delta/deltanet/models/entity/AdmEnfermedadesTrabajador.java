package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "adm_enfermedades_trabajador")
public class AdmEnfermedadesTrabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trabajador_id", nullable = false)
    private AdmDatosTrabajador trabajador;

    @Column(name = "nombre_enfermedad", nullable = false)
    private String nombreEnfermedad;

    @Column(name = "tratamiento")
    private String tratamiento;

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

    public String getNombreEnfermedad() {
        return nombreEnfermedad;
    }

    public void setNombreEnfermedad(String nombreEnfermedad) {
        this.nombreEnfermedad = nombreEnfermedad;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
}
