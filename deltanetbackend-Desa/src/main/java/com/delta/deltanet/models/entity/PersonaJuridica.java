package com.delta.deltanet.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Entity
@Table(name="per_jur")
public class PersonaJuridica implements Serializable {
   
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long idPerJur;

    @Column(name="razon_social", length = 200)
    public String razonSocial;

    @Column(name="razon_comercial", length = 200)
    public String razonComercial;

    @Column(name="fec_inicio_oper")
    @Temporal(TemporalType.TIMESTAMP)
    public Date fecIniOper;

    public String tipo;

    public Long estado;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona",cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Direccion> direcciones;

    public Long getIdPerJur() {
        return idPerJur;
    }

    public void setIdPerJur(Long idPerJur) {
        this.idPerJur = idPerJur;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRazonComercial() {
        return razonComercial;
    }

    public void setRazonComercial(String razonComercial) {
        this.razonComercial = razonComercial;
    }

    public Date getFecIniOper() {
        return fecIniOper;
    }

    public void setFecIniOper(Date fecIniOper) {
        this.fecIniOper = fecIniOper;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }
}
