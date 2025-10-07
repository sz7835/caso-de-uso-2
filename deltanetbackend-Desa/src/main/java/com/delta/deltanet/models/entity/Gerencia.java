package com.delta.deltanet.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Entity
@Table(name = "adm_org_gerencias")
public class Gerencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "estado")
    private int estado;

    @Column(name = "create_user", length = 20, nullable = false)
    private String usuCreado;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreado;

    @Column(name = "update_user", length = 20)
    private String usuUpdate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUpdate;

    @OneToMany(mappedBy = "gerencia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "gerencia" })
    private List<OrgAreas> orgAreas;

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEstado() {
        return estado;
    }

    public String getUsuCreado() {
        return usuCreado;
    }

    public Date getFechaCreado() {
        return fechaCreado;
    }

    public String getUsuUpdate() {
        return usuUpdate;
    }

    public Date getFechaUpdate() {
        return fechaUpdate;
    }

    public List<OrgAreas> getOrgAreas() {
        return orgAreas;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setUsuCreado(String usuCreado) {
        this.usuCreado = usuCreado;
    }

    public void setFechaCreado(Date fechaCreado) {
        this.fechaCreado = fechaCreado;
    }

    public void setUsuUpdate(String usuUpdate) {
        this.usuUpdate = usuUpdate;
    }

    public void setFechaUpdate(Date fechaUpdate) {
        this.fechaUpdate = fechaUpdate;
    }

    public void setOrgAreas(List<OrgAreas> orgAreas) {
        this.orgAreas = orgAreas;
    }
}
