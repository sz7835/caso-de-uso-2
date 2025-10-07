package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adm_calendario_feriado_generado_por")
public class AdmCalendarioFeriadoGeneradoPor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calendario_feriado_generado_por")
    private Long idCalendarioFeriadoGeneradoPor;

    @Column(name = "descripcion", length = 100, nullable = false)
    private String descripcion;

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
    public Long getIdCalendarioFeriadoGeneradoPor() {
        return idCalendarioFeriadoGeneradoPor;
    }

    public String getDescripcion() {
        return descripcion;
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
    public void setIdCalendarioFeriadoGeneradoPor(Long idCalendarioFeriadoGeneradoPor) {
        this.idCalendarioFeriadoGeneradoPor = idCalendarioFeriadoGeneradoPor;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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