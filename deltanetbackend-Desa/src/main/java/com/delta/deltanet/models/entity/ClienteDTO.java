package com.delta.deltanet.models.entity;

import java.util.Date;

public class ClienteDTO {
  private Long id;
  private String razonSocial;
  private String razonComercial;
  private Date fecInicioOper;
  private String tipo;
  private Long estado;

  // Getters and setters
  public Long getIdPerJur() {
      return id;
  }

  public void setIdPerJur(Long idPerJur) {
      this.id = idPerJur;
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
      return fecInicioOper;
  }

  public void setFecIniOper(Date fecIniOper) {
      this.fecInicioOper = fecIniOper;
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

  public static ClienteDTO fromPersona(Persona persona) {
      ClienteDTO dto = new ClienteDTO();
      dto.setIdPerJur(persona.getId());
      if (persona.getPerJur() != null) {
          PersonaJuridica perJur = persona.getPerJur();
          dto.setRazonSocial(perJur.getRazonSocial());
          dto.setRazonComercial(perJur.getRazonComercial());
          dto.setFecIniOper(perJur.getFecIniOper());
          dto.setTipo(perJur.getTipo());
          dto.setEstado(perJur.getEstado());
      }
      return dto;
  }
}