package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@Entity
@Table(name = "per_nat")
public class PersonaNatural implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long idPerNat;

    @Column(name="nombres")
    public String nombre;

    @Column(name="ape_paterno")
    public String apePaterno;

    @Column(name="ape_materno")
    public String apeMaterno;

    @Column(name="fec_nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    public Date fecNacim;

    @OneToOne
    @JoinColumn(name="id_sexo")
    public Sexo sex;

    @OneToOne
    @JoinColumn(name="id_estado_civil")
    public TipoEstadoCivil estCivil;

    public Double peso;
    public Integer estatura;

    /*
    @OneToOne
    @JoinColumn(name="id_enfermedad")
    public Enfermedad enfermedad;

    @OneToOne
    @JoinColumn(name = "id_discapacidad")
    public Discapacidad discapacidad;
    public String tratamiento;

     */

    public Long getIdPerNat() {
        return idPerNat;
    }

    public void setIdPerNat(Long idPerNat) {
        this.idPerNat = idPerNat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApePaterno() {
        return apePaterno;
    }

    public void setApePaterno(String apePaterno) {
        this.apePaterno = apePaterno;
    }

    public String getApeMaterno() {
        return apeMaterno;
    }

    public void setApeMaterno(String apeMaterno) {
        this.apeMaterno = apeMaterno;
    }

    public Date getFecNacim() {
        return fecNacim;
    }

    public void setFecNacim(Date fecNacim) {
        this.fecNacim = fecNacim;
    }

    public Sexo getSex() {
        return sex;
    }

    public void setSex(Sexo sex) {
        this.sex = sex;
    }

    public TipoEstadoCivil getEstCivil() {
        return estCivil;
    }

    public void setEstCivil(TipoEstadoCivil estCivil) {
        this.estCivil = estCivil;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getEstatura() {
        return estatura;
    }

    public void setEstatura(Integer estatura) {
        this.estatura = estatura;
    }

    /*
    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public Discapacidad getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Discapacidad discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
     */
}
