package com.delta.deltanet.models.entity;

import javax.persistence.*;
<<<<<<< HEAD
import java.io.Serializable;

@Entity
@Table(name = "per_nat_sexo")
public class PerNatSexo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

=======

@Entity
@Table(name = "per_nat_sexo")
public class PerNatSexo {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "descripcion")
    private String descripcion;

    public PerNatSexo() {}

>>>>>>> 7fc03ff (chore(release): v2.0 – back to plain single controller)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
