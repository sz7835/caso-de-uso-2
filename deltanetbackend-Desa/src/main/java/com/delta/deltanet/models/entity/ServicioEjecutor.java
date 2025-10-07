package com.delta.deltanet.models.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="tkt_servicios_ejecutor")
public class ServicioEjecutor {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Long idUsuario;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private AutenticacionUsuario usuario;

    @Column(name = "id_area",insertable = false, updatable = false)
    private Long idArea;

    @OneToOne
    @JoinColumn(name = "id_area")
    private Areas area;

    @Column(name = "estado")
    private int estado;

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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public AutenticacionUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(AutenticacionUsuario usuario) {
        this.usuario = usuario;
    }

    public Long getIdArea() {
        return idArea;
    }

    public void setIdArea(Long idArea) {
        this.idArea = idArea;
    }

    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
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
