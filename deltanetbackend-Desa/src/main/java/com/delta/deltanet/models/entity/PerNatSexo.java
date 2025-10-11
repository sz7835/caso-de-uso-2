package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "per_nat_sexo")
public class PerNatSexo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
