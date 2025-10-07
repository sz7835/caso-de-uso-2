package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adm_calendario_semana_dia_tipo")
public class AdmTipoDiaSemana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calendario_semana_dia_tipo")
    private Long idCalendarioSemana;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "acronimo", length = 25, nullable = false)
    private String acronimo;

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
    public Long getIdCalendarioSemana() {
        return idCalendarioSemana;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAcronimo() {
        return acronimo;
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
    public void setIdCalendarioSemana(Long idCalendarioSemana) {
        this.idCalendarioSemana = idCalendarioSemana;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
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
