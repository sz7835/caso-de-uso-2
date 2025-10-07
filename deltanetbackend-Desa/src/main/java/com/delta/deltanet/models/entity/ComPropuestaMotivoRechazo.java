package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "com_propuesta_motivo_rechazo")
public class ComPropuestaMotivoRechazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "com_propuesta_id", nullable = false)
    private Long comPropuesta;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @Column(name = "fecha", nullable = true)
    private LocalDate fecha;

    @Column(name = "create_user", length = 50, nullable = false)
    private String createUser;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_user", length = 50, nullable = true)
    private String updateUser;

    @Column(name = "update_date", nullable = true)
    private LocalDateTime updateDate;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComPropuesta() {
        return comPropuesta;
    }

    public void setComPropuesta(Long comPropuesta) {
        this.comPropuesta = comPropuesta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
