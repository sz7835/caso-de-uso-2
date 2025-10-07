package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adm_calendario_feriado")
public class OutFeriado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_calendario_feriado")
    private Long id;

    @Column(name = "id_anio")
    private Long idAnio;

    @Column(name = "id_feriado_generado_por")
    private Long idFeriadoGeneradoPor;

    @Column(name = "motivo_feriado", length = 191)
    private String descripcion;

    @Column(name = "es_aplicable_sector_privado", length = 1)
    private String esAplicableSectorPrivado;
    
    @Column(name = "es_aplicable_sector_publico", length = 1)
    private String esAplicableSectorPublico;

    @Column(name = "fecha_feriado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "id_semana_dia")
    private Long idSemanaDia;

    @Column(name = "estado")
    private Long estado;
    
    @Column(name = "create_user",length = 20)
    private String createUser;
    
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    @Column(name = "update_user",length = 20)
    private String updateUser;
    
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdAnio() {
		return idAnio;
	}

	public void setIdAnio(Long idAnio) {
		this.idAnio = idAnio;
	}

	public Long getIdFeriadoGeneradoPor() {
		return idFeriadoGeneradoPor;
	}

	public void setIdFeriadoGeneradoPor(Long idFeriadoGeneradoPor) {
		this.idFeriadoGeneradoPor = idFeriadoGeneradoPor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEsAplicableSectorPrivado() {
		return esAplicableSectorPrivado;
	}

	public void setEsAplicableSectorPrivado(String esAplicableSectorPrivado) {
		this.esAplicableSectorPrivado = esAplicableSectorPrivado;
	}

	public String getEsAplicableSectorPublico() {
		return esAplicableSectorPublico;
	}

	public void setEsAplicableSectorPublico(String esAplicableSectorPublico) {
		this.esAplicableSectorPublico = esAplicableSectorPublico;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getIdSemanaDia() {
		return idSemanaDia;
	}

	public void setIdSemanaDia(Long idSemanaDia) {
		this.idSemanaDia = idSemanaDia;
	}

	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
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
