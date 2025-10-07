package com.delta.deltanet.models.entity;

public class RelacionDTO {
    private Long idRel;
    private Long idPersona;
    private String nombrePersona;

    // Getters
    public Long getIdRel() {
        return idRel;
    }

    public void setIdRel(Long idRel) {
        this.idRel = idRel;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }
}
