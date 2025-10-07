package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "out_registro_proyecto")
public class RegistroProyecto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(name = "codigo", nullable = false)
    private String codigo;
    
    @Column(name = "descripcion", nullable = false)
    private String proyectoDescripcion;

    @Column(name = "estado", nullable = false)
    private int estado;

    @Column(name = "create_user", nullable = false)
    private String createUser;

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getProyectoDescripcion() {
        return proyectoDescripcion;
    }

    public void setProyectoDescripcion(String proyectoDescripcion) {
        this.proyectoDescripcion = proyectoDescripcion;
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

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public RegistroProyecto() {
		super();
	}
    
	public RegistroProyecto(Long idPersona, String codigo, String proyectoDescripcion, int estado, String createUser,
			Date createDate) {
		super();
		this.idPersona = idPersona;
		this.codigo = codigo;
		this.proyectoDescripcion = proyectoDescripcion;
		this.estado = estado;
		this.createUser = createUser;
		this.createDate = createDate;
	}

}