package com.delta.deltanet.models.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "adm_ale_msj_cumple")
public class AdmAleMsjCumple implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_sexo", nullable = false)
    private Integer idSexo;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "lista_corr_defecto", length = 1000)
    private String listaCorrDefecto;

    @Lob
    @Column(name = "msg_header_banner")
    private byte[] msgHeaderBanner;

    @Lob
    @Column(name = "msg_header_image")
    private byte[] msgHeaderImage;

    @Column(name = "msg_body", length = 1000)
    private String msgBody;

    @Column(name = "msg_footer", length = 250)
    private String msgFooter;

    @Column(name = "create_user", nullable = false, length = 50)
    private String createUser;

    // -- Getters & Setters --

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSexo() {
        return idSexo;
    }
    public void setIdSexo(Integer idSexo) {
        this.idSexo = idSexo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getListaCorrDefecto() {
        return listaCorrDefecto;
    }
    public void setListaCorrDefecto(String listaCorrDefecto) {
        this.listaCorrDefecto = listaCorrDefecto;
    }

    public byte[] getMsgHeaderBanner() {
        return msgHeaderBanner;
    }
    public void setMsgHeaderBanner(byte[] msgHeaderBanner) {
        this.msgHeaderBanner = msgHeaderBanner;
    }

    public byte[] getMsgHeaderImage() {
        return msgHeaderImage;
    }
    public void setMsgHeaderImage(byte[] msgHeaderImage) {
        this.msgHeaderImage = msgHeaderImage;
    }

    public String getMsgBody() {
        return msgBody;
    }
    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getMsgFooter() {
        return msgFooter;
    }
    public void setMsgFooter(String msgFooter) {
        this.msgFooter = msgFooter;
    }

    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
