package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "adm_contrato_cronograma_vigente")
public class AdmCronograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_contrato")
    private int idContrato;

    @Column(name = "id_modalidad")
    private int idModalidad;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "id_colaborador")
    private int idConsultor;

    @Column(name = "periodo_inicial")
    private LocalDate periodoInicial;

    @Column(name = "periodo_final")
    private LocalDate periodoFinal;

    @Column(name = "oc_nro", length = 45)
    private String nroOc;

    @Column(name = "oc_fecha")
    private LocalDate fechaOc;

    @Column(name = "hes")
    private int hes;

    @Column(name = "monto")
    private Double monto;

    @Column(name = "estado")
    private int estado;

    @Column(name = "create_user", length = 20)
    private String createUser;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_user", length = 20)
    private String updateUser;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public AdmCronograma() {
        // Constructor vacío
    }

    // Constructor con parámetros
    public AdmCronograma(int idContrato, String descripcion, int idConsultor, String servicioEn,
                          String proyectoRq, String capexOpex, BigDecimal tarifario, LocalDate periodoInicial,
                          LocalDate periodoFinal, BigDecimal costoMensual, String nroOc, LocalDate fechaOc,
                          int estado, String createUser, LocalDateTime createDate, String updateUser,
                          LocalDateTime updateDate) {
        this.idContrato = idContrato;
        this.descripcion = descripcion;
        this.idConsultor = idConsultor;
        this.periodoInicial = periodoInicial;
        this.periodoFinal = periodoFinal;
        this.nroOc = nroOc;
        this.fechaOc = fechaOc;
        this.estado = estado;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateUser = updateUser;
        this.updateDate = updateDate;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdConsultor() {
        return idConsultor;
    }

    public void setIdConsultor(int idConsultor) {
        this.idConsultor = idConsultor;
    }

    public LocalDate getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(LocalDate periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public LocalDate getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(LocalDate periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getNroOc() {
        return nroOc;
    }

    public void setNroOc(String nroOc) {
        this.nroOc = nroOc;
    }

    public LocalDate getFechaOc() {
        return fechaOc;
    }

    public void setFechaOc(LocalDate fechaOc) {
        this.fechaOc = fechaOc;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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

    public int getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }

    public int getHes() {
        return hes;
    }

    public void setHes(int hes) {
        this.hes = hes;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
