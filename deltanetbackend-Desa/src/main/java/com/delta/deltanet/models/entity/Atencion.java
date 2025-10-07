package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="per_atencion")
public class Atencion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int id;

    @Column(name="id_tipo_persona")
    public int idTipoPer;

    @Column(name="abreviacion")
    public String abrev;

    @Column(name="descripcion")
    public String descrip;

    @Column(name="estado")
    public int estado;

    @Column(name = "create_user", length = 20)
    public String creUser;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date creDate;

    @Column(name = "update_user", length = 20)
    public String updUser;

    @Column(name="update_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date updDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTipoPer() {
        return idTipoPer;
    }

    public void setIdTipoPer(int idTipoPer) {
        this.idTipoPer = idTipoPer;
    }

    public String getAbrev() {
        return abrev;
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    public Date getCreDate() {
        return creDate;
    }

    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }
}
