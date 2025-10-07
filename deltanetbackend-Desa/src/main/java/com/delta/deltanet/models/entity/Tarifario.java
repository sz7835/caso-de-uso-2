package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name = "com_tarifario")
public class Tarifario implements Serializable {

	private static final long serialVersionUID = 1L;

@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "fecha_ini", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date fechaIni;

  @Column(name = "fecha_fin", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date fechaFin;

  @Column(name = "perfil", nullable = false)
  private int perfil;

  @Column(name = "lugar", nullable = false)
  private int lugar;

  @Column(name = "monto", nullable = false, precision = 18, scale = 2)
  private BigDecimal monto;

  @Column(name = "moneda", nullable = false)
  private int moneda;

  @Column(name = "estado", nullable = false)
  private int estado;

  @Column(name = "create_user", length = 50, nullable = false)
  private String createUser;

  @Column(name = "create_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate;

  @Column(name = "update_user", length = 50)
  private String updateUser;

  @Column(name = "update_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updateDate;

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public int getPerfil() {
    return perfil;
  }

  public void setPerfil(int perfil) {
    this.perfil = perfil;
  }

  public int getLugar() {
    return lugar;
  }

  public void setLugar(int lugar) {
    this.lugar = lugar;
  }

  public BigDecimal getMonto() {
    return monto;
  }

  public void setMonto(BigDecimal monto) {
    this.monto = monto;
  }

  public int getMoneda() {
    return moneda;
  }

  public void setMoneda(int moneda) {
    this.moneda = moneda;
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
}
