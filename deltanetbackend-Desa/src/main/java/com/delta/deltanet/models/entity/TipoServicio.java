package com.delta.deltanet.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adm_contrato_tipo_servicio")
public class TipoServicio implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo")
    private TipoContrato tipo;

    @Column(name="id_tipo", insertable = false, updatable = false)
    private Long idTipo;

    @Column(name="nombre")
    private String descrip;

    private Integer estado;

    @Column(name = "create_user")
    private String usrCreate;

    @Column(name = "create_date")
    private Date fecCreate;

    @Column(name = "update_user")
    private String usrUpdate;

    @Column(name = "update_date")
    private Date fecUpdate;

    //Getter and Setter


    public TipoContrato getTipo() {
		return tipo;
	}

	public void setTipo(TipoContrato tipo) {
		this.tipo = tipo;
	}

	public Long getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}

    public Long getId() {
        return id;
    }
    
	public void setId(Long id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getUsrCreate() {
        return usrCreate;
    }

    public void setUsrCreate(String usrCreate) {
        this.usrCreate = usrCreate;
    }

    public Date getFecCreate() {
        return fecCreate;
    }

    public void setFecCreate(Date fecCreate) {
        this.fecCreate = fecCreate;
    }

    public String getUsrUpdate() {
        return usrUpdate;
    }

    public void setUsrUpdate(String usrUpdate) {
        this.usrUpdate = usrUpdate;
    }

    public Date getFecUpdate() {
        return fecUpdate;
    }

    public void setFecUpdate(Date fecUpdate) {
        this.fecUpdate = fecUpdate;
    }
}
