package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adm_calendario_historico")
public class CalendarioHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calendario_historico")
    private Long id;

    @Column(name = "id_anio")
    private Long idAnio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anio", insertable = false, updatable = false)
    private AdmCalendarioAnio calendarioAnio;

    @Column(name = "id_mes")
    private Long idMes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mes", insertable = false, updatable = false)
    private AdmCalendarioMes calendarioMes;

    @Column(name = "id_semana_dia")
    private Long idSemanaDia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_semana_dia", insertable = false, updatable = false)
    private AdmCalendarioSemanaDia calendarioSemanaDia;

    @Column(name = "id_semana_dia_tipo")
    private Long idSemanaDiaTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_semana_dia_tipo", insertable = false, updatable = false)
    private AdmTipoDiaSemana calendarioSemanaDiaTipo;

    @Column(name = "id_feriado_historico", nullable = true)
    private Long idFeriado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_feriado_historico", insertable = false, updatable = false)
    private AdmCalendarioFeriadoHistorico calendarioFeriado;

    @Column(name = "contador_dia")
    private Long contadorDia;

    @Column(name = "fecha_calendario")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "es_feriado_empresa", length = 1)
    private String esAplicableSectorPrivado;

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

	public Long getIdMes() {
		return idMes;
	}

	public void setIdMes(Long idMes) {
		this.idMes = idMes;
	}

	public Long getIdSemanaDia() {
		return idSemanaDia;
	}

	public void setIdSemanaDia(Long idSemanaDia) {
		this.idSemanaDia = idSemanaDia;
	}

	public Long getIdSemanaDiaTipo() {
		return idSemanaDiaTipo;
	}

	public void setIdSemanaDiaTipo(Long idSemanaDiaTipo) {
		this.idSemanaDiaTipo = idSemanaDiaTipo;
	}

	public Long getIdFeriado() {
		return idFeriado;
	}

	public void setIdFeriado(Long idFeriado) {
		this.idFeriado = idFeriado;
	}

	public Long getContadorDia() {
		return contadorDia;
	}

	public void setContadorDia(Long contadorDia) {
		this.contadorDia = contadorDia;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEsAplicableSectorPrivado() {
		return esAplicableSectorPrivado;
	}

	public void setEsAplicableSectorPrivado(String esAplicableSectorPrivado) {
		this.esAplicableSectorPrivado = esAplicableSectorPrivado;
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

	public AdmCalendarioAnio getCalendarioAnio() {
		return calendarioAnio;
	}

	public void setCalendarioAnio(AdmCalendarioAnio calendarioAnio) {
		this.calendarioAnio = calendarioAnio;
	}

	public AdmCalendarioSemanaDia getCalendarioSemanaDia() {
		return calendarioSemanaDia;
	}

	public void setCalendarioSemanaDia(AdmCalendarioSemanaDia calendarioSemanaDia) {
		this.calendarioSemanaDia = calendarioSemanaDia;
	}

	public AdmTipoDiaSemana getCalendarioSemanaDiaTipo() {
		return calendarioSemanaDiaTipo;
	}

	public void setCalendarioSemanaDiaTipo(AdmTipoDiaSemana calendarioSemanaDiaTipo) {
		this.calendarioSemanaDiaTipo = calendarioSemanaDiaTipo;
	}

	public AdmCalendarioFeriadoHistorico getCalendarioFeriado() {
		return calendarioFeriado;
	}

	public void setCalendarioFeriado(AdmCalendarioFeriadoHistorico calendarioFeriado) {
		this.calendarioFeriado = calendarioFeriado;
	}

	public AdmCalendarioMes getCalendarioMes() {
		return calendarioMes;
	}

	public void setCalendarioMes(AdmCalendarioMes calendarioMes) {
		this.calendarioMes = calendarioMes;
	}

}
