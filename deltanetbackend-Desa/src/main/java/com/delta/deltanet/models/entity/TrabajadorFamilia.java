package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adm_trabajador_familiar")
public class TrabajadorFamilia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_trabajador")
    private Long idPerNat;

    @OneToOne
    @JoinColumn(name = "id_trabajador", insertable = false, updatable = false)
    private PersonaNatural perNat;

    private Integer convivencia;

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

    // Getters y setters
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

    public Integer getConvivencia() {
        return convivencia;
    }

    public void setConvivencia(Integer convivencia) {
        this.convivencia = convivencia;
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
