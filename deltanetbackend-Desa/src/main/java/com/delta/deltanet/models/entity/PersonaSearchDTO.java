package com.delta.deltanet.models.entity;

import java.util.Date;

public class PersonaSearchDTO {
	private Long idPer;
	private Long idTipoPer;
    private String tipoDoc;
    private String nroDoc;
    private String nombre;
    private String apePat;
    private String apeMat;
    private Date fechaNacimiento;
    private Sexo sexo;
    private int estado;
    
    
	public Long getIdPer() {
		return idPer;
	}
	public void setIdPer(Long idPer) {
		this.idPer = idPer;
	}
	public Long getIdTipoPer() {
		return idTipoPer;
	}
	public void setIdTipoPer(Long idTipoPer) {
		this.idTipoPer = idTipoPer;
	}
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getNroDoc() {
		return nroDoc;
	}
	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getApePat() {
		return apePat;
	}
	public void setApePat(String apePat) {
		this.apePat = apePat;
	}
	public String getApeMat() {
		return apeMat;
	}
	public void setApeMat(String apeMat) {
		this.apeMat = apeMat;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public Sexo getSexo() {
		return sexo;
	}
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

    
}