package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "adm_discapacidades_trabajador")
public class AdmDiscapacidadesTrabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trabajador_id", nullable = false)
    private AdmDatosTrabajador trabajador;

    @Column(name = "tipo_discapacidad", nullable = false)
    private String tipoDiscapacidad;

    @Column(name = "ajustes_razonables")
    private String ajustesRazonables;

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

    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    public String getAjustesRazonables() {
        return ajustesRazonables;
    }

    public void setAjustesRazonables(String ajustesRazonables) {
        this.ajustesRazonables = ajustesRazonables;
    }
}
