package com.delta.deltanet.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name="adm_contrato")
public class Contrato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "id_enlace")
    private Long idRelacion;

    @OneToOne
    @JoinColumn(name = "id_enlace",insertable = false,updatable = false)
    private Relacion relacion;

    @Column(name = "id_tipo_contrato", nullable = true)
    private Long idTipoContrato;

    @Column(name="alerta_vencimiento")
    public Integer vencimiento;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_contrato", insertable = false, updatable = false)
    private TipoContrato typeContract;

    @OneToOne
    @JoinColumn(name = "id_tipo_contrato",insertable = false,updatable = false, nullable = true)
    private TipoContrato contrato;

    @Column(name = "id_tipo_servicio", nullable = true)
    private Long idTipoServicio;
    
    @Column(name = "id_tipo_persona", insertable = false,updatable = false,nullable = true)
    private Long idTipoPersona;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_servicio", insertable = false, updatable = false, nullable = true)
    private TipoServicio typeService;

    @OneToOne
    @JoinColumn(name = "id_tipo_servicio",insertable = false,updatable = false, nullable = true)
    private TipoServicio service;

    @Column(name = "id_forma_pago", nullable = true)
    private Long idFormaPago;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", insertable = false, updatable = false)
    private Persona client;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_forma_pago", insertable = false, updatable = false, nullable = true)
    private FormaPago payment;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @Column(name = "fec_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIni;

    @Column(name = "fec_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecFin;

    @Column(name = "monto", nullable = true)
    private Double monto;

    @Column(name = "hes")
    private Long hes;
    
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @Column(name = "id_modalidad")
    private Long idModalidad;
    
    @Column(name = "puesto")
    private String puesto;

    @Column(name = "estado")
    private Long estado;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user")
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

    public Long getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(Long idRelacion) {
        this.idRelacion = idRelacion;
    }

    public Relacion getRelacion() {
        return relacion;
    }

    public void setRelacion(Relacion relacion) {
        this.relacion = relacion;
    }

    public TipoContrato getContrato() {
		return contrato;
	}

	public void setContrato(TipoContrato contrato) {
		this.contrato = contrato;
	}

	public TipoServicio getService() {
		return service;
	}

	public void setService(TipoServicio service) {
		this.service = service;
	}

    public Long getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(Long idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    public Integer getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Integer vencimiento) {
		this.vencimiento = vencimiento;
	}

	public Long getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(Long idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public Long getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Long idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Long getHes() {
        return hes;
    }

    public void setHes(Long hes) {
        this.hes = hes;
    }
    
    public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdModalidad() {
		return idModalidad;
	}

	public void setIdModalidad(Long idModalidad) {
		this.idModalidad = idModalidad;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
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
