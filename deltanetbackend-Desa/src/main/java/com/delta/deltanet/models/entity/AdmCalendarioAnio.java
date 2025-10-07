package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adm_calendario_anio")
public class AdmCalendarioAnio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calendario_anio")
    private Integer idCalendarioAnio;

    @ManyToOne
    @JoinColumn(name = "id_anio_estado", nullable = false)
    private AdmCalendarioAnioEstado anioEstado;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "create_user", length = 20, nullable = false)
    private String createUser;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_user", length = 20)
    private String updateUser;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    // Getters
    public Integer getIdCalendarioAnio() {
        return idCalendarioAnio;
    }

    public AdmCalendarioAnioEstado getAnioEstado() {
        return anioEstado;
    }

    public Integer getIdAnioEstado() {
        return anioEstado != null ? anioEstado.getIdAnioEstado().intValue() : null;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getEstado() {
        return estado;
    }

    public String getCreateUser() {
        return createUser;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    // Setters
    public void setIdCalendarioAnio(Integer idCalendarioAnio) {
        this.idCalendarioAnio = idCalendarioAnio;
    }

    public void setAnioEstado(AdmCalendarioAnioEstado anioEstado) {
        this.anioEstado = anioEstado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
} 