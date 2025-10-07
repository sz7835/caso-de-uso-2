package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.security.SecureRandomParameters;

@Entity
@Table(name="ubi_distrito")
public class Distrito implements SecureRandomParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long id;

    @Column(name = "nombre", length = 100)
    public String nombre;
    @ManyToOne
    @JoinColumn(name="per_provincia_id")
    public Provincia provincia;

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

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}
