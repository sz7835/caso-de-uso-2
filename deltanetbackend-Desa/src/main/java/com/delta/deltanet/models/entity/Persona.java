package com.delta.deltanet.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Entity
@Table(name="per_persona")
public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_tipo_persona")
    public TipoPersona tipoPer;

    @ManyToOne
    @JoinColumn(name="id_tipo_documento")
    public TipoDocumento tipoDoc;

    @Column(name="documento",length = 45)
    public String documento;

    //@Column(name="tratamiento")
    //public Integer tratamiento;

    @OneToOne
    @JoinColumn(name="tratamiento")
    public Atencion atencion;

    @Column(name="estado")
    public int estado;

    @Column(name="visible")
    public int visible;

    @OneToOne
    @JoinColumn(name="id_datos_persona_natural")
    public PersonaNatural perNat;

    @OneToOne
    @JoinColumn(name="id_datos_persona_juridica")
    public PersonaJuridica perJur;

    @Column(name="id_titular", nullable=true)
    public Long idTitular;

    @Column(name="id_nodo_tipo", nullable=true)
    public Long idNodoTipo;

    @OneToOne
    @JoinColumn(name="id_area", nullable=true)
    public OrgAreas area;

    @OneToOne
    @JoinColumn(name="id_puesto", nullable=true)
    public Puestos puesto;

    @Column(name = "create_user", length = 20)
    public String creUser;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date creDate;

    @Column(name = "update_user", length = 20)
    public String updUser;

    @Column(name="update_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date updDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Telefono> telefonos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<EMail> correos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona",cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Direccion> direcciones;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Relacion> relaciones;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<Cronograma> cronogramas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<PersonaCliente> clients;


    public List<Telefono> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;
    }

    public List<EMail> getCorreos() {
        return correos;
    }

    public void setCorreos(List<EMail> correos) {
        this.correos = correos;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public List<Relacion> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(List<Relacion> relaciones) {
        this.relaciones = relaciones;
    }

    public List<Cronograma> getCronogramas() {
        return cronogramas;
    }

    public void setCronogramas(List<Cronograma> cronogramas) {
        this.cronogramas = cronogramas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoPersona getTipoPer() {
        return tipoPer;
    }

    public void setTipoPer(TipoPersona tipoPer) {
        this.tipoPer = tipoPer;
    }

    public TipoDocumento getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDocumento tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Atencion getAtencion() {
        return atencion;
    }

    public void setAtencion(Atencion atencion) {
        this.atencion = atencion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public PersonaNatural getPerNat() {
        return perNat;
    }

    public void setPerNat(PersonaNatural perNat) {
        this.perNat = perNat;
    }

    public PersonaJuridica getPerJur() {
        return perJur;
    }

    public void setPerJur(PersonaJuridica perJur) {
        this.perJur = perJur;
    }

    public Long getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(Long idTitular) {
        this.idTitular = idTitular;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    public Date getCreDate() {
        return creDate;
    }

    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
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

	public List<PersonaCliente> getClients() {
		return clients;
	}

	public void setClients(List<PersonaCliente> clients) {
		this.clients = clients;
	}
	
	public PersonaSearchDTO toPersonaSearchDto() {
		PersonaSearchDTO personaSearchDTO = new PersonaSearchDTO();
		personaSearchDTO.setIdPer(this.id);
		personaSearchDTO.setTipoDoc(this.getTipoDoc().getNombre());
		personaSearchDTO.setNroDoc(this.getDocumento());
		personaSearchDTO.setEstado(this.getEstado());
		personaSearchDTO.setIdTipoPer(this.getTipoPer().getIdTipoPer());
		if(this.getPerNat() != null) {
			personaSearchDTO.setNombre(this.getPerNat().getNombre());
			personaSearchDTO.setApePat(this.getPerNat().getApePaterno());
			personaSearchDTO.setApeMat(this.getPerNat().getApeMaterno());
			personaSearchDTO.setFechaNacimiento(this.getPerNat().getFecNacim());
			personaSearchDTO.setSexo(this.getPerNat().getSex());
		}else {
			personaSearchDTO.setNombre(this.getPerJur().getRazonSocial() + " " + this.getPerJur().getRazonComercial());
			personaSearchDTO.setFechaNacimiento(this.getPerJur().getFecIniOper());
		}
		return personaSearchDTO;
	}
}
