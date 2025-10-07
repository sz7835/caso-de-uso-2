package com.delta.deltanet.models.entity;

public class lstContract {
    public Long idContrato;
    public String client;
    public String descripcion;
    public String tipo;
    public String periodoInicial;
    public String periodoFinal;
    public String fechaOC;
    public Long estado;
    public String descEstado;
    public String porVencer;
    public boolean vencido;
    
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Long getIdContrato() {
		return idContrato;
	}
	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getPeriodoInicial() {
		return periodoInicial;
	}
	public void setPeriodoInicial(String periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	public String getPeriodoFinal() {
		return periodoFinal;
	}
	public void setPeriodoFinal(String periodoFinal) {
		this.periodoFinal = periodoFinal;
	}
	public String getFechaOC() {
		return fechaOC;
	}
	public void setFechaOC(String fechaOC) {
		this.fechaOC = fechaOC;
	}
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}
	public String getDescEstado() {
		return descEstado;
	}
	public void setDescEstado(String descEstado) {
		this.descEstado = descEstado;
	}
	public String getPorVencer() {
		return porVencer;
	}
	public void setPorVencer(String porVencer) {
		this.porVencer = porVencer;
	}
	public boolean isVencido() {
		return vencido;
	}
	public void setVencido(boolean vencido) {
		this.vencido = vencido;
	}
}