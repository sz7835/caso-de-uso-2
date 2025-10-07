package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adm_trabajador_sueldo_pension")
public class TrabajadorSueldo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_trabajador")
    private Long idPerNat;

    @OneToOne
    @JoinColumn(name = "id_trabajador", insertable = false, updatable = false)
    private PersonaNatural perNat;

    @Column(name = "id_sbs_afp")
    private Integer idAFP;

    @OneToOne
    @JoinColumn(name = "id_sbs_afp", insertable = false, updatable = false)
    private SbsAfp afp;

    @Column(name = "id_sbs_banca")
    private Integer idBanca;

    @OneToOne
    @JoinColumn(name = "id_sbs_banca", insertable = false, updatable = false)
    private SbsBanca banca;

    @Column(name = "numero_afp")
    private String nroAfp;

    @Column(name = "numero_cuenta")
    private String nroCuenta;

    @Column(name = "numero_interbancario")
    private String nroCCI;

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

    public Integer getIdAFP() {
        return idAFP;
    }

    public void setIdAFP(Integer idAFP) {
        this.idAFP = idAFP;
    }

    public SbsAfp getAfp() {
        return afp;
    }

    public void setAfp(SbsAfp afp) {
        this.afp = afp;
    }

    public Integer getIdBanca() {
        return idBanca;
    }

    public void setIdBanca(Integer idBanca) {
        this.idBanca = idBanca;
    }

    public SbsBanca getBanca() {
        return banca;
    }

    public void setBanca(SbsBanca banca) {
        this.banca = banca;
    }

    public String getNroAfp() {
        return nroAfp;
    }

    public void setNroAfp(String nroAfp) {
        this.nroAfp = nroAfp;
    }

    public String getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    public String getNroCCI() {
        return nroCCI;
    }

    public void setNroCCI(String nroCCI) {
        this.nroCCI = nroCCI;
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
