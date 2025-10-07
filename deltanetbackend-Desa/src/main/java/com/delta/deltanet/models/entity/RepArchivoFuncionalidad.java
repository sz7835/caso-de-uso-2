package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rep_archivo_modulo_sub_funcionalidad")
public class RepArchivoFuncionalidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idArchivoFunc;

    @ManyToOne
    @JoinColumn(name="id_folder")
    public AdmFolder folder;

    @Column(name = "id_folder", insertable = false, updatable = false)
    private Long idFolder;

    @Column(name = "clave")
    private String clave;

    @Column(name = "nombre_funcionalidad")
    private String nombre;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "create_user")
    private String usrCreate;

    @Column(name = "create_date")
    private Date fecCreate;

    @Column(name = "update_user")
    private String usrUpdate;

    @Column(name = "update_date")
    private Date fecUpdate;

    public Long getIdArchivoFunc() {
        return idArchivoFunc;
    }

    public void setIdArchivoFunc(Long idArchivoFunc) {
        this.idArchivoFunc = idArchivoFunc;
    }

    public Long getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(Long idFolder) {
        this.idFolder = idFolder;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getClave() {
    	return clave;
    }

    public void setClave(String clave) {
    	this.clave = clave;
    }

	public AdmFolder getFolder() {
		return folder;
	}

	public void setFolder(AdmFolder folder) {
		this.folder = folder;
	}

}
