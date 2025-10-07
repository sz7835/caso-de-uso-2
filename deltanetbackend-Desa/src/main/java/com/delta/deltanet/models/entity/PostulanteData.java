package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name = "com_postulante")
public class PostulanteData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellido_pat", length = 100, nullable = false)
    private String apellidoPat;

    @Column(name = "apellido_mat", length = 100, nullable = false)
    private String apellidoMat;

    @Column(name = "fecha_nac", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNac;

    @Column(name = "domicilio", length = 255)
    private String domicilio;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "nro_telefono", length = 15)
    private String nroTelefono;

    @Column(name = "formacion_academica")
    private Integer formacionAcademica;

    @Column(name = "grado_academico")
    private Integer gradoAcademico;

    @Column(name = "colegiatura", columnDefinition = "INT DEFAULT 0")
    private Integer colegiatura;

    @Column(name = "experiencia_general")
    private Integer experienciaGeneral;

    @Column(name = "experiencia_especifica")
    private Integer experienciaEspecifica;

    @Column(name = "experiencia_especifica_anios")
    private Integer aniosExperiencia;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "create_user", length = 50)
    private String createUser;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_user", length = 50)
    private String updateUser;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNroTelefono() {
        return nroTelefono;
    }

    public void setNroTelefono(String nroTelefono) {
        this.nroTelefono = nroTelefono;
    }

    public Integer getFormacionAcademica() {
        return formacionAcademica;
    }

    public void setFormacionAcademica(Integer formacionAcademica) {
        this.formacionAcademica = formacionAcademica;
    }

    public Integer getGradoAcademico() {
        return gradoAcademico;
    }

    public void setGradoAcademico(Integer gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public Integer getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(Integer colegiatura) {
        this.colegiatura = colegiatura;
    }

    public Integer getExperienciaGeneral() {
        return experienciaGeneral;
    }

    public void setExperienciaGeneral(Integer experienciaGeneral) {
        this.experienciaGeneral = experienciaGeneral;
    }

    public Integer getExperienciaEspecifica() {
        return experienciaEspecifica;
    }

    public void setExperienciaEspecifica(Integer experienciaEspecifica) {
        this.experienciaEspecifica = experienciaEspecifica;
    }

    public Integer getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(Integer aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
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
