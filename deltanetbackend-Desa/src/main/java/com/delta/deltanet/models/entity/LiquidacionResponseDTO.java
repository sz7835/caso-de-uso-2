package com.delta.deltanet.models.entity;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.delta.deltanet.models.service.IRepArchivoFuncService;
import com.delta.deltanet.models.service.IRepArchivoService;

@SuppressWarnings("all")
@Component
public class LiquidacionResponseDTO {

    private static IRepArchivoService repArchivoService;

    private static IRepArchivoFuncService archivoFuncService;

    @Autowired
    private IRepArchivoService iRepArchivoService;
    @Autowired
    private IRepArchivoFuncService iRepArchivoFuncService;

    @PostConstruct
    public void init() {
        this.repArchivoService = iRepArchivoService;
        this.archivoFuncService = iRepArchivoFuncService;
    }

    private Long id;
    private Integer idCliente;
    private ClienteDTO datosCliente;
    private int idContrato;
    private Date fechaRegistro;
    private BigDecimal monto;
    private String observaciones;
    private int estado;
    private String documento;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private int hes;
    private String url;

    // Getters and setters

    public static LiquidacionResponseDTO fromLiquidacion(Liquidaciones liquidacion, Persona persona) {
        LiquidacionResponseDTO dto = new LiquidacionResponseDTO();
        dto.setId(liquidacion.getId());
        dto.setIdCliente(liquidacion.getIdCliente());
        dto.setDocumento(persona.getDocumento());
        dto.setDatosCliente(ClienteDTO.fromPersona(persona));
        dto.setIdContrato(liquidacion.getIdContrato());
        dto.setFechaRegistro(liquidacion.getFechaRegistro());
        dto.setMonto(liquidacion.getMonto());
        dto.setObservaciones(liquidacion.getObservaciones());
        dto.setEstado(liquidacion.getEstado());
        dto.setCreateUser(liquidacion.getCreateUser());
        dto.setCreateDate(liquidacion.getCreateDate());
        dto.setUpdateUser(liquidacion.getUpdateUser());
        dto.setUpdateDate(liquidacion.getUpdateDate());
        List<RepArchivo> listaObj = repArchivoService.getRepArchivos(liquidacion.getId(), "administracion_liquidation");
        int response = 0;
        String url = "";
        if (listaObj.size() > 0) {
            for (Iterator<RepArchivo> iterator = listaObj.iterator(); iterator.hasNext();) {
                RepArchivo repArchivo = (RepArchivo) iterator.next();
                Optional<RepArchivoFuncionalidad> file = archivoFuncService
                        .busca(repArchivo.getIdModuloFuncionalidad());
                if (!file.isEmpty()) {
                    if(file.get().getClave().equalsIgnoreCase("hes")) {
	                    response = 1;
	                    url = repArchivo.getUrl();
	                    break;
                    }
                }
            }
        }
        dto.setUrl(url);
        dto.setHes(response);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public ClienteDTO getDatosCliente() {
        return datosCliente;
    }

    public void setDatosCliente(ClienteDTO datosCliente) {
        this.datosCliente = datosCliente;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public int getHes() {
        return hes;
    }

    public void setHes(int hes) {
        this.hes = hes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

}