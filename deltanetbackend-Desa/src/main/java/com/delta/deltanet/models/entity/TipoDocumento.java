package com.delta.deltanet.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("all")
@Entity
@Table(name="per_persona_documento_tipo")
public class TipoDocumento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long idTipDoc;

    @Column(name="nombre",length = 45)
    public String nombre;

    @Column(name="longitud")
    public int longitud;

    @Column(name="formato",length = 45)
    public String formato;

    public Long getIdTipDoc() {
        return idTipDoc;
    }

    public void setIdTipDoc(Long idTipDoc) {
        this.idTipDoc = idTipDoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
}
