package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rep_archivo")
public class RepArchivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long idArchivo;

    @Column(name = "id_folder")
    private Long idFolder;

    @Column(name = "id_modulo_funcionalidad")
    private Long idModuloFuncionalidad;

    @Column(name = "archivo_nombre")
    private String nombre;

    @Column(name = "id_archivo_formato")
    private Long idArchivoFormato;

    @Column(name = "tabla_registro_id")
    private Long idtabla;

    @Column(name = "tabla_registro_nombre")
    private String tabla;

    @Column(name = "archivo_nombre_encriptado")
    private String encriptado;

    @Column(name = "archivo_url_rep")
    private String url;

    @Column(name = "create_user")
    private String usrCreate;

    @Column(name = "create_date")
    private Date fecCreate;

    @Column(name = "update_user")
    private String usrUpdate;

    @Column(name = "update_date")
    private Date fecUpdate;

    public Long getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Long getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(Long idFolder) {
        this.idFolder = idFolder;
    }

    public Long getIdModuloFuncionalidad() {
        return idModuloFuncionalidad;
    }

    public void setIdModuloFuncionalidad(Long idModuloFuncionalidad) {
        this.idModuloFuncionalidad = idModuloFuncionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdArchivoFormato() {
        return idArchivoFormato;
    }

    public void setIdArchivoFormato(Long idArchivoFormato) {
        this.idArchivoFormato = idArchivoFormato;
    }

    public Long getIdtabla() {
        return idtabla;
    }

    public void setIdtabla(Long idtabla) {
        this.idtabla = idtabla;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getEncriptado() {
        return encriptado;
    }

    public void setEncriptado(String encriptado) {
        this.encriptado = encriptado;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
