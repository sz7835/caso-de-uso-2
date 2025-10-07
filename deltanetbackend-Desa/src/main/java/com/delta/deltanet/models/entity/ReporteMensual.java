package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "out_reporte_mensual")
public class ReporteMensual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_tipo_reporte_mensual")
    private Long idTipoReporteMensual;

    @Column(name = "id_persona")
    private Long idPersona;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_user")
    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTipoReporteMensual() {
        return idTipoReporteMensual;
    }

    public void setIdTipoReporteMensual(Long idTipoReporteMensual) {
        this.idTipoReporteMensual = idTipoReporteMensual;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
