package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="adm_liquidacion_pago")
public class LiquidacionPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_tipo_operacion", insertable = false, updatable = false)
    private Long idLiquidacion;

	@ManyToOne
	@JoinColumn(name = "id_liquidacion")
	private Liquidaciones liquidacion;

    @Column(name = "id_tipo_operacion", insertable = false, updatable = false)
    private Long idTipoOperacion;

    @ManyToOne
	@JoinColumn(name = "id_tipo_operacion")
	private LiquidacionTipoOperacion type;

    @Column(name = "id_banco", insertable = false, updatable = false)
    private Long idBanco;

    @ManyToOne
	@JoinColumn(name = "id_banco")
	private SbsBanca bank;

    @Column(name = "nro_operacion", length = 45)
    private String nroOperacion;

    @Column(name = "monto", nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

	@Column(name = "fecha_pago", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaPago;

    @Column(name = "estado", nullable = false)
    private int estado;

    @Column(name = "create_user", length = 20, nullable = false)
    private String createUser;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user", length = 20)
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

	public Long getIdLiquidacion() {
		return idLiquidacion;
	}

	public void setIdLiquidacion(Long idLiquidacion) {
		this.idLiquidacion = idLiquidacion;
	}

	public Liquidaciones getLiquidacion() {
		return liquidacion;
	}

	public void setLiquidacion(Liquidaciones liquidacion) {
		this.liquidacion = liquidacion;
	}

	public Long getIdTipoOperacion() {
		return idTipoOperacion;
	}

	public void setIdTipoOperacion(Long idTipoOperacion) {
		this.idTipoOperacion = idTipoOperacion;
	}

	public LiquidacionTipoOperacion getType() {
		return type;
	}

	public void setType(LiquidacionTipoOperacion type) {
		this.type = type;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public SbsBanca getBank() {
		return bank;
	}

	public void setBank(SbsBanca bank) {
		this.bank = bank;
	}

	public String getNroOperacion() {
		return nroOperacion;
	}

	public void setNroOperacion(String nroOperacion) {
		this.nroOperacion = nroOperacion;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
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
