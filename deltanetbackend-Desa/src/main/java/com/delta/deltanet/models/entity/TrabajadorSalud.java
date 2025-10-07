package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adm_trabajador_salud")
public class TrabajadorSalud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_trabajador")
    private Long idPerNat;

    @OneToOne
    @JoinColumn(name = "id_trabajador", insertable = false, updatable = false)
    private PersonaNatural perNat;

    @Column(name = "discapacidad_flag")
    private Integer flagDiscap;

    @Column(name = "discapacidad_id")
    private Integer idDiscap;

    @OneToOne
    @JoinColumn(name = "discapacidad_id", insertable = false, updatable = false)
    private Discapacidad discapacidad;

    @Column(name = "discapacidad_ajustes")
    private String ajusteDiscap;

    @Column(name = "enfermedad_flag")
    private Integer flagEnfer;

    @Column(name = "enfermedad_id")
    private Integer idEnfer;

    @OneToOne
    @JoinColumn(name = "enfermedad_id", insertable = false, updatable = false)
    private Enfermedad enfermedad;

    @Column(name = "enfermedad_tratamiento")
    private String tratamEnfer;

    private Integer estado;

    @Column(name = "create_user", length = 45)
    private String createUser;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user", length = 45)
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

    public Long getIdPerNat() {
        return idPerNat;
    }

    public void setIdPerNat(Long idPerNat) {
        this.idPerNat = idPerNat;
    }

    public PersonaNatural getPerNat() {
        return perNat;
    }

    public void setPerNat(PersonaNatural perNat) {
        this.perNat = perNat;
    }

    public Integer getFlagDiscap() {
        return flagDiscap;
    }

    public void setFlagDiscap(Integer flagDiscap) {
        this.flagDiscap = flagDiscap;
    }

    public Integer getIdDiscap() {
        return idDiscap;
    }

    public void setIdDiscap(Integer idDiscap) {
        this.idDiscap = idDiscap;
    }

    public Discapacidad getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Discapacidad discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getAjusteDiscap() {
        return ajusteDiscap;
    }

    public void setAjusteDiscap(String ajusteDiscap) {
        this.ajusteDiscap = ajusteDiscap;
    }

    public Integer getFlagEnfer() {
        return flagEnfer;
    }

    public void setFlagEnfer(Integer flagEnfer) {
        this.flagEnfer = flagEnfer;
    }

    public Integer getIdEnfer() {
        return idEnfer;
    }

    public void setIdEnfer(Integer idEnfer) {
        this.idEnfer = idEnfer;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getTratamEnfer() {
        return tratamEnfer;
    }

    public void setTratamEnfer(String tratamEnfer) {
        this.tratamEnfer = tratamEnfer;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
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
