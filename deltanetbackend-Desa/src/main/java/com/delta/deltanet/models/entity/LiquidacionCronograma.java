package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "adm_liquidacion_cronogramas")
public class LiquidacionCronograma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "id_cronograma")
	private Long idCronograma;

	@OneToOne
	@JoinColumn(name = "id_cronograma", insertable = false, updatable = false)
	private Cronograma cronograma;

	@Column(name = "id_liquidacion")
	private Long idLiquidacion;

	@OneToOne
	@JoinColumn(name = "id_liquidacion", insertable = false, updatable = false)
	private Liquidaciones liquidation;

	public Cronograma getCronograma() {
		return cronograma;
	}

	public void setCronograma(Cronograma cronograma) {
		this.cronograma = cronograma;
	}

	public Liquidaciones getLiquidation() {
		return liquidation;
	}

	public void setLiquidation(Liquidaciones liquidation) {
		this.liquidation = liquidation;
	}

// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCronograma() {
		return idCronograma;
	}

	public void setIdCronograma(Long idCronograma) {
		this.idCronograma = idCronograma;
	}

	public Long getIdLiquidacion() {
		return idLiquidacion;
	}

	public void setIdLiquidacion(Long idLiquidacion) {
		this.idLiquidacion = idLiquidacion;
	}
}
