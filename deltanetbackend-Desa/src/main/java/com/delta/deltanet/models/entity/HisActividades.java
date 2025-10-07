package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="out_his_actividades")
public class HisActividades implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_cliente", nullable = true)
    private Long idCliente;

    @Column(name = "razon_social", length = 200, nullable = true)
    private String razonSocial;

    @Column(name = "id_persona")
    private Long idPersona;

    @Column(name = "nom_colab", length = 200)
    private String nomColab;

    @Column(length = 6)
    private String periodo;

    @Column(name = "id_proyecto")
    private Integer idProyecto;

    @Column(length = 15)
    private String proyecto;

    @Column(name = "id_reg_horas")
    private Integer idRegHoras;

    @Column(name = "fec_reg_hora")
    private Date fecRegHora;

    @Column(name = "dia_semana")
    private String diaSemana;

    @Column(name = "nro_horas")
    private Integer nroHoras;

    @Column(name = "dsc_actividad", length = 250)
    private String dscActividad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getNomColab() {
        return nomColab;
    }

    public void setNomColab(String nomColab) {
        this.nomColab = nomColab;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public Integer getIdRegHoras() {
        return idRegHoras;
    }

    public void setIdRegHoras(Integer idRegHoras) {
        this.idRegHoras = idRegHoras;
    }

    public Date getFecRegHora() {
        return fecRegHora;
    }

    public void setFecRegHora(Date fecRegHora) {
        this.fecRegHora = fecRegHora;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getNroHoras() {
        return nroHoras;
    }

    public void setNroHoras(Integer nroHoras) {
        this.nroHoras = nroHoras;
    }

    public String getDscActividad() {
        return dscActividad;
    }

    public void setDscActividad(String dscActividad) {
        this.dscActividad = dscActividad;
    }
}
