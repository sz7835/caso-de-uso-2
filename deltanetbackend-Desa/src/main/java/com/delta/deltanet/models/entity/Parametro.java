package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="per_params")
public class Parametro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long idParam;

    @Column(name="id_rubro_par")
    private Long idRubro;

    @Column(name="id_item")
    private Long idItem;

    @Column(name="valor_item")
    private String valItem;

    public Long getIdParam() {
        return idParam;
    }

    public void setIdParam(Long idParam) {
        this.idParam = idParam;
    }

    public Long getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public String getValItem() {
        return valItem;
    }

    public void setValItem(String valItem) {
        this.valItem = valItem;
    }
}
