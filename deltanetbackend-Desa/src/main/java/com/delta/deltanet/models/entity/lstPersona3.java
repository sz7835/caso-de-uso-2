package com.delta.deltanet.models.entity;

import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
public class lstPersona3 {
    public Long id;
    public Long tipPer;
    public Long tipDoc;
    public Long idNodoTipo;
    public String nroDoc;
    public Integer tratamiento;
    public String nombre;
    public String apellidoP;
    public String apellidoM;
    public Sexo sexo;
    public TipoEstadoCivil estadoCivil;
    public Double peso;
    public Integer estatura;
    public Integer estado;
    public Date fecCrea;
    public String fecIni;
    public String razcom;
    public Long idTitular;
    public String nombreTitular;
    public OrgAreas area;
    public Puestos puesto;
    public Long idArea;
    public Long idPuesto;
    public List<lstTelefono1> telefonos;
    public List<lstEmail1> correos;
    public List<lstDireccion1> direcciones;
    private List<lstPersona1> personasNaturales;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTipPer() {
        return tipPer;
    }

    public void setTipPer(Long tipPer) {
        this.tipPer = tipPer;
    }

    public Long getTipDoc() {
        return tipDoc;
    }

    public void setTipDoc(Long tipDoc) {
        this.tipDoc = tipDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public Integer getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Integer tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public TipoEstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(TipoEstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Date getFecCrea() {
        return fecCrea;
    }

    public void setFecCrea(Date fecCrea) {
        this.fecCrea = fecCrea;
    }

    public String getFecIni() {
        return fecIni;
    }

    public void setFecIni(String fecIni) {
        this.fecIni = fecIni;
    }

    public String getRazcom() {
        return razcom;
    }

    public void setRazcom(String razcom) {
        this.razcom = razcom;
    }

    public Long getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(Long idTitular) {
        this.idTitular = idTitular;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public List<lstTelefono1> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<lstTelefono1> telefonos) {
        this.telefonos = telefonos;
    }

    public List<lstEmail1> getCorreos() {
        return correos;
    }

    public void setCorreos(List<lstEmail1> correos) {
        this.correos = correos;
    }

    public List<lstDireccion1> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<lstDireccion1> direcciones) {
        this.direcciones = direcciones;
    }

    public List<lstPersona1> getPersonasNaturales() {
        return personasNaturales;
    }

    public void setPersonasNaturales(List<lstPersona1> personasNaturales) {
        this.personasNaturales = personasNaturales;
    }

	public Long getIdNodoTipo() {
		return idNodoTipo;
	}

	public void setIdNodoTipo(Long idNodoTipo) {
		this.idNodoTipo = idNodoTipo;
	}

	public OrgAreas getArea() {
		return area;
	}

	public void setArea(OrgAreas area) {
		this.area = area;
	}

	public Puestos getPuesto() {
		return puesto;
	}

	public void setPuesto(Puestos puesto) {
		this.puesto = puesto;
	}

	public Long getIdArea() {
		return idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public Long getIdPuesto() {
		return idPuesto;
	}

	public void setIdPuesto(Long idPuesto) {
		this.idPuesto = idPuesto;
	}

	public String getApellidoP() {
		return apellidoP;
	}

	public void setApellidoP(String apellidoP) {
		this.apellidoP = apellidoP;
	}

	public String getApellidoM() {
		return apellidoM;
	}

	public void setApellidoM(String apellidoM) {
		this.apellidoM = apellidoM;
	}

}
