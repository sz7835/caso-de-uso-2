package com.delta.deltanet.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adm_org_jefaturas")
public class Areas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_gerencia", insertable = false, updatable = false)
    private Integer idGerencia;

    @OneToOne
    @JoinColumn(name = "id_gerencia")
    @JsonBackReference
    private Gerencia gerencia;

    private String nombre;
    private Integer estado;

    @Column(name = "create_user")
    private String usrCreate;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreate;

    @Column(name = "update_user")
    private String usrUpdate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecUpdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdGerencia() {
        return idGerencia;
    }

    public void setIdGerencia(Integer idGerencia) {
        this.idGerencia = idGerencia;
    }

    public Gerencia getGerencia() {
        return gerencia;
    }

    public void setGerencia(Gerencia gerencia) {
        this.gerencia = gerencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getUsrCreate() {
        return usrCreate;
    }

    public void setUsrCreate(String usrCreate) {
        this.usrCreate = usrCreate;
    }

    public Date getFecCreate() {
        return fecCreate;
    }

    public void setFecCreate(Date fecCreate) {
        this.fecCreate = fecCreate;
    }

    public String getUsrUpdate() {
        return usrUpdate;
    }

    public void setUsrUpdate(String usrUpdate) {
        this.usrUpdate = usrUpdate;
    }

    public Date getFecUpdate() {
        return fecUpdate;
    }

    public void setFecUpdate(Date fecUpdate) {
        this.fecUpdate = fecUpdate;
    }
}
