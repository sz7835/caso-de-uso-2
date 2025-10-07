package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adm_contrato")
public class ContratoOutsourcing implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="id_enlace")
    public Relacion relacion;

    @OneToOne
    @JoinColumn(name="id_tipo_contrato")
    public TipoContrato tipoContrato;

    @Column(name="alerta_vencimiento")
    public Integer vencimiento;

	@OneToOne
    @JoinColumn(name="id_tipo_persona")
    public NodoTipo tipoPersona;

    @OneToOne
    @JoinColumn(name="id_tipo_servicio")
    public TipoServicio tipoServicio;

    @OneToOne
    @JoinColumn(name="id_forma_pago")
    public FormaPago formaPago;

    private String descripcion;

    @Column(name="fec_inicio")
    @Temporal(TemporalType.DATE)
    private Date fecInicio;

    @Column(name="fec_fin")
    @Temporal(TemporalType.DATE)
    private Date fecFin;
    @Column(precision = 18, scale = 2)
    private Double monto;
    private Long hes;
    private Long estado;
    
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @OneToOne
    @JoinColumn(name="id_modalidad")
    private Modalidad modalidad;
    
    @Column(name = "puesto")
    private String puesto;

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

    //Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Relacion getRelacion() {
        return relacion;
    }

    public void setRelacion(Relacion relacion) {
        this.relacion = relacion;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }
    
    public NodoTipo getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(NodoTipo tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Integer getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Integer vencimiento) {
		this.vencimiento = vencimiento;
	}

	public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecInicio() {
        return fecInicio;
    }

    public void setFecInicio(Date fecInicio) {
        this.fecInicio = fecInicio;
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

	public Modalidad getModalidad() {
		return modalidad;
	}

	public void setModalidad(Modalidad modalidad) {
		this.modalidad = modalidad;
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
