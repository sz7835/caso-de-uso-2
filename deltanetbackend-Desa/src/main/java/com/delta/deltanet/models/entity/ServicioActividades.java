package com.delta.deltanet.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="srv_actividades")
public class ServicioActividades {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@OneToOne
	@JoinColumn(name="id_cli")
	private PersonaJuridica cliente;
	
	@OneToOne
	@JoinColumn(name="id_tipo")
	private ServicioTipo tipo;
	
	@OneToOne
	@JoinColumn(name="id_forma")
	private ServicioForma forma;
	
	@OneToOne
	@JoinColumn(name="id_estado")
	private SrvEstado estado;
	
	@OneToOne
	@JoinColumn(name="id_resolutor")
	private AutenticacionUsuario resolutor;

	@Column(name = "numero")
	private Long numero;
	
	@Column(name = "descripcion")
	private String descrip;
	
	@Column(name = "nro_horas")
	private double horas;
	
	@Column(name = "fec_ini")
    private Date fechaIni;
	
	@Column(name = "fec_fin")
    private Date fechaFin;
	
	@Column(name = "mes_eje")
	private String mesEje;
	
	@Column(name = "year_eje")
	private String yearEje;
	
	@Column(name = "mes_fac", nullable = true)
	private String mesFac;
	
	@Column(name = "year_fac", nullable = true)
	private String yearFac;
	
	@Column(name = "facturable")
	private Integer facturable;
	
	@Column(name = "observacion")
	private String obs;
	
	@Column(name = "create_user")
    private String createUser;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_date")
    private Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PersonaJuridica getCliente() {
		return cliente;
	}

	public void setCliente(PersonaJuridica cliente) {
		this.cliente = cliente;
	}

	public ServicioTipo getTipo() {
		return tipo;
	}

	public void setTipo(ServicioTipo tipo) {
		this.tipo = tipo;
	}

	public ServicioForma getForma() {
		return forma;
	}

	public void setForma(ServicioForma forma) {
		this.forma = forma;
	}

	public SrvEstado getEstado() {
		return estado;
	}

	public void setEstado(SrvEstado estado) {
		this.estado = estado;
	}

	public AutenticacionUsuario getResolutor() {
		return resolutor;
	}

	public void setResolutor(AutenticacionUsuario resolutor) {
		this.resolutor = resolutor;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public double getHoras() {
		return horas;
	}

	public void setHoras(double horas) {
		this.horas = horas;
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

	public String getMesEje() {
		return mesEje;
	}

	public void setMesEje(String mesEje) {
		this.mesEje = mesEje;
	}

	public String getYearEje() {
		return yearEje;
	}

	public void setYearEje(String yearEje) {
		this.yearEje = yearEje;
	}

	public String getMesFac() {
		return mesFac;
	}

	public void setMesFac(String mesFac) {
		this.mesFac = mesFac;
	}

	public String getYearFac() {
		return yearFac;
	}

	public void setYearFac(String yearFac) {
		this.yearFac = yearFac;
	}

	public Integer getFacturable() {
		return facturable;
	}

	public void setFacturable(Integer facturable) {
		this.facturable = facturable;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
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
