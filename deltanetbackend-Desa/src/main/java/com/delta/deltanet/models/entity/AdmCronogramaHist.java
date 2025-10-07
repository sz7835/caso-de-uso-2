package com.delta.deltanet.models.entity;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adm_contrato_cronograma_historico")
public class AdmCronogramaHist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cronograma")
    private Long idCrono;

    @Column(name = "id_contrato")
    private Long idContrato;

    @Column(name = "id_modalidad")
    private Long idModalidad;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "id_colaborador")
    private Long idConsultor;

    @Column(name = "periodo_inicial")
    @Temporal(TemporalType.DATE)
    private Date periodoInicial;

    @Column(name = "periodo_final")
    @Temporal(TemporalType.DATE)
    private Date periodoFinal;

    @Column(name = "oc_nro", length = 45)
    private String nroOc;

    @Column(name = "oc_fecha")
    @Temporal(TemporalType.DATE)
    private Date fechaOc;

    @Column(name = "hes")
    private int hes;

    @Column(name = "monto")
    private Double monto;

    @Column(name = "estado")
    private Long estado;

    @Column(name = "create_user", length = 20)
    private String createUser;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user", length = 20)
    private String updateUser;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCrono() {
        return idCrono;
    }

    public void setIdCrono(Long idCrono) {
        this.idCrono = idCrono;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(Long idModalidad) {
        this.idModalidad = idModalidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdConsultor() {
        return idConsultor;
    }

    public void setIdConsultor(Long idConsultor) {
        this.idConsultor = idConsultor;
    }

    public Date getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Date periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public Date getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getNroOc() {
        return nroOc;
    }

    public void setNroOc(String nroOc) {
        this.nroOc = nroOc;
    }

    public Date getFechaOc() {
        return fechaOc;
    }

    public void setFechaOc(Date fechaOc) {
        this.fechaOc = fechaOc;
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

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
