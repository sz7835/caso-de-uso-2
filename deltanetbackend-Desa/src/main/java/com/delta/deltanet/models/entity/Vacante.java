package com.delta.deltanet.models.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name = "com_vacante")
public class Vacante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "puesto_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PuestoOcupacional puesto;

    @Column(name = "puesto_id", nullable = false)
    private int puestoId;

    @Column(name = "fechaini", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaIni;

    @Column(name = "fechafin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @ManyToOne
    @JoinColumn(name = "foracad_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteFormacion forAcad;

    @Column(name = "foracad_id", nullable = false)
    private int forAcadId;

    @ManyToOne
    @JoinColumn(name = "gradsitacad_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteGrado gradSitAcad;

    @Column(name = "gradsitacad_id", nullable = false)
    private int gradSitAcadId;

    @Column(name = "colegiatura", nullable = false)
    private boolean colegiatura;

    @Column(name = "expgeneral")
    private Integer expGeneral;

    @ManyToOne
    @JoinColumn(name = "expespec_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PostulanteExperiencia expEspec;

    @Column(name = "expespec_id", nullable = false)
    private int expEspecId;

    @Column(name = "expreq")
    private Integer expReq;

    @Column(name = "conocimientos", columnDefinition = "TEXT")
    private String conocimientos;

    @Column(name = "especializacion", columnDefinition = "TEXT")
    private String especializacion;

    @Column(name = "estado", nullable = false)
    private int estado;

    @Column(name = "createuser", length = 50, nullable = false)
    private String createUser;

    @Column(name = "createdate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "updateuser", length = 50)
    private String updateUser;

    @Column(name = "updatedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPuestoId() {
        return puestoId;
    }

    public void setPuestoId(int puestoId) {
        this.puestoId = puestoId;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getForAcadId() {
        return forAcadId;
    }

    public void setForAcadId(int forAcadId) {
        this.forAcadId = forAcadId;
    }

    public int getGradSitAcadId() {
        return gradSitAcadId;
    }

    public void setGradSitAcadId(int gradSitAcadId) {
        this.gradSitAcadId = gradSitAcadId;
    }

    public boolean isColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(boolean colegiatura) {
        this.colegiatura = colegiatura;
    }

    public Integer getExpGeneral() {
        return expGeneral;
    }

    public void setExpGeneral(Integer expGeneral) {
        this.expGeneral = expGeneral;
    }

    public int getExpEspecId() {
        return expEspecId;
    }

    public void setExpEspecId(int expEspecId) {
        this.expEspecId = expEspecId;
    }

    public Integer getExpReq() {
        return expReq;
    }

    public void setExpReq(Integer expReq) {
        this.expReq = expReq;
    }

    public String getConocimientos() {
        return conocimientos;
    }

    public void setConocimientos(String conocimientos) {
        this.conocimientos = conocimientos;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
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

    public PuestoOcupacional getPuesto() {
        return puesto;
    }

    public void setPuesto(PuestoOcupacional puesto) {
        this.puesto = puesto;
    }

    public PostulanteFormacion getForAcad() {
        return forAcad;
    }

    public void setForAcad(PostulanteFormacion forAcad) {
        this.forAcad = forAcad;
    }

    public PostulanteGrado getGradSitAcad() {
        return gradSitAcad;
    }

    public void setGradSitAcad(PostulanteGrado gradSitAcad) {
        this.gradSitAcad = gradSitAcad;
    }

    public PostulanteExperiencia getExpEspec() {
        return expEspec;
    }

    public void setExpEspec(PostulanteExperiencia expEspec) {
        this.expEspec = expEspec;
    }
}