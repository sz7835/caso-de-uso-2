package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.FetchType;

@Entity
@Table(name="sec_autenticacion_usuario")
public class AutenticacionUsuario implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "usuario", length = 20, nullable = false)
	private String usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_persona")
    private Persona persona;

    @Column(name = "SEC_PASSWORD")
    private int secPassword;

    @Column(name = "TIPO_USER_AMBITO")
    private int tipoUsuarioAmbito;

    @Column(name = "TIPO_USER_CATEGORIA")
    private int tipoUsuario;

    @Column(name = "TIPO_USER_SISTEMA")
    private int tipoUsuarioDelta;
    
    @Column(name = "COD_EST_USUARIO", nullable = false)
    private int codEstUsuario;

    @Column(name = "NUM_INTENTOS")
    private int numIntentos;

    @Column(name = "USR_INGRESO", nullable = false)
    private String usrIngreso;
    
    @Column(name = "FEC_INGRESO")
	@Temporal(TemporalType.TIMESTAMP)
    private Date fecIngreso;

    @Column(name = "USR_ULT_MOD")
    private String usrUltMod;

    @Column(name = "FEC_ULT_MOD")
	@Temporal(TemporalType.TIMESTAMP)
    private Date fecUltMod;
    
    @Column(name = "ENROLADO_SERVICE")
    private int enroladoService;
    
    @Column(name = "AUTORIZADO")
    private Integer autorizado;

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public int getSecPassword() {
        return secPassword;
    }

    public void setSecPassword(int secPassword) {
        this.secPassword = secPassword;
    }

    public int getTipoUsuarioAmbito() {
        return tipoUsuarioAmbito;
    }

    public void setTipoUsuarioAmbito(int tipoUsuarioAmbito) {
        this.tipoUsuarioAmbito = tipoUsuarioAmbito;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getTipoUsuarioDelta() {
        return tipoUsuarioDelta;
    }

    public void setTipoUsuarioDelta(int tipoUsuarioDelta) {
        this.tipoUsuarioDelta = tipoUsuarioDelta;
    }

    public int getCodEstUsuario() {
        return codEstUsuario;
    }

    public void setCodEstUsuario(int codEstUsuario) {
        this.codEstUsuario = codEstUsuario;
    }

    public int getNumIntentos() {
        return numIntentos;
    }

    public void setNumIntentos(int numIntentos) {
        this.numIntentos = numIntentos;
    }

    public String getUsrIngreso() {
        return usrIngreso;
    }

    public void setUsrIngreso(String usrIngreso) {
        this.usrIngreso = usrIngreso;
    }

    public Date getFecIngreso() {
        return fecIngreso;
    }

    public void setFecIngreso(Date fecIngreso) {
        this.fecIngreso = fecIngreso;
    }

    public String getUsrUltMod() {
        return usrUltMod;
    }

    public void setUsrUltMod(String usrUltMod) {
        this.usrUltMod = usrUltMod;
    }

    public Date getFecUltMod() {
        return fecUltMod;
    }

    public void setFecUltMod(Date fecUltMod) {
        this.fecUltMod = fecUltMod;
    }

    public int getEnroladoService() {
        return enroladoService;
    }

    public void setEnroladoService(int enroladoService) {
        this.enroladoService = enroladoService;
    }

    public Integer getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Integer autorizado) {
        this.autorizado = autorizado;
    }

	private static final long serialVersionUID = 1L;
}
