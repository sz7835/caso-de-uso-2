package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adm_calendario_feriado_historico")
public class AdmCalendarioFeriadoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feriado_historico")
    private Long idCalendarioFeriado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anio", insertable = false, updatable = false)
    private AdmCalendarioAnio calendarioAnio;
    
    @Column(name = "id_anio", nullable = false)
    private Long idAnio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_feriado_generado_por", insertable = false, updatable = false)
    private AdmCalendarioFeriadoGeneradoPor feriadoGeneradoPor;
    
    @Column(name = "id_feriado_generado_por", nullable = false)
    private Long idFeriadoGeneradoPor;
    
    @Column(name = "motivo_feriado", length = 100, nullable = false)
    private String motivo;

    @Column(name = "es_aplicable_sector_privado", length = 1, nullable = false)
    private String aplicableSectorPrivado;

    @Column(name = "es_aplicable_sector_publico", length = 1, nullable = false)
    private String aplicableSectorPublico;

    @Column(name = "fecha_feriado", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_semana_dia", insertable = false, updatable = false)
    private AdmCalendarioSemanaDia semanaDia;
    
    @Column(name = "id_semana_dia", nullable = false)
    private Long idSemanaDia;
    
    @Column(name = "estado", nullable = false)
    private Long estado;

    @Column(name = "create_user", length = 20, nullable = false)
    private String createUser;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "update_user", length = 20)
    private String updateUser;

    @Column(name = "update_date")
    private Date updateDate;

	public Long getIdCalendarioFeriado() {
		return idCalendarioFeriado;
	}

	public void setIdCalendarioFeriado(Long idCalendarioFeriado) {
		this.idCalendarioFeriado = idCalendarioFeriado;
	}

	public AdmCalendarioAnio getCalendarioAnio() {
		return calendarioAnio;
	}

	public void setCalendarioAnio(AdmCalendarioAnio calendarioAnio) {
		this.calendarioAnio = calendarioAnio;
	}

	public Long getIdAnio() {
		return idAnio;
	}

	public void setIdAnio(Long idAnio) {
		this.idAnio = idAnio;
	}

	public AdmCalendarioFeriadoGeneradoPor getFeriadoGeneradoPor() {
		return feriadoGeneradoPor;
	}

	public void setFeriadoGeneradoPor(AdmCalendarioFeriadoGeneradoPor feriadoGeneradoPor) {
		this.feriadoGeneradoPor = feriadoGeneradoPor;
	}

	public Long getIdFeriadoGeneradoPor() {
		return idFeriadoGeneradoPor;
	}

	public void setIdFeriadoGeneradoPor(Long idFeriadoGeneradoPor) {
		this.idFeriadoGeneradoPor = idFeriadoGeneradoPor;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getAplicableSectorPrivado() {
		return aplicableSectorPrivado;
	}

	public void setAplicableSectorPrivado(String aplicableSectorPrivado) {
		this.aplicableSectorPrivado = aplicableSectorPrivado;
	}

	public String getAplicableSectorPublico() {
		return aplicableSectorPublico;
	}

	public void setAplicableSectorPublico(String aplicableSectorPublico) {
		this.aplicableSectorPublico = aplicableSectorPublico;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public AdmCalendarioSemanaDia getSemanaDia() {
		return semanaDia;
	}

	public void setSemanaDia(AdmCalendarioSemanaDia semanaDia) {
		this.semanaDia = semanaDia;
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
