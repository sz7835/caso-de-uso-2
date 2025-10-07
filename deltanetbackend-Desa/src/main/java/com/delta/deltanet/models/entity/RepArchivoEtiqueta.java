package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rep_archivo_etiqueta")
public class RepArchivoEtiqueta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long idArchivoEtiqueta;

    @Column(name = "etiqueta_nombre")
    private String nombre;

    @Column(name = "etiqueta_descripcion")
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name="id_folder")
    public AdmFolder folder;

    @Column(name = "id_folder", insertable = false, updatable = false)
    private Long idFolder;
    
    @Column(name = "etiqueta_estado")
    private Integer estado;

    @Column(name = "create_user")
    private String usrCreate;

    @Column(name = "create_date")
    private Date fecCreate;

    @Column(name = "update_user")
    private String usrUpdate;

    @Column(name = "update_date")
    private Date fecUpdate;

    public Long getIdArchivoEtiqueta() {
        return idArchivoEtiqueta;
    }

    public void setIdArchivoEtiqueta(Long idArchivoEtiqueta) {
        this.idArchivoEtiqueta = idArchivoEtiqueta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdFolder() {
		return idFolder;
	}

	public AdmFolder getFolder() {
		return folder;
	}

	public void setFolder(AdmFolder folder) {
		this.folder = folder;
	}

	public void setIdFolder(Long idFolder) {
		this.idFolder = idFolder;
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
