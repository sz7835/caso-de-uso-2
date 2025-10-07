package com.delta.deltanet.models.entity;

public class RelacionContactosDTO {
	private Long idRel;
    private Long idPer;
    private Long idTipoPer;
    private String nombre;
    private String tipoDoc;
    private String nroDoc;
    private String motivo;
    private Long idMotivo;
    private Long estado;
	public Long getIdRel() {
		return idRel;
	}
	public void setIdRel(Long idRel) {
		this.idRel = idRel;
	}
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Long getIdMotivo() {
		return idMotivo;
	}
	public void setIdMotivo(Long idMotivo) {
		this.idMotivo = idMotivo;
	}
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}


}