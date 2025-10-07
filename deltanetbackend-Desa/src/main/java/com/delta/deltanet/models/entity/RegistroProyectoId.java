package com.delta.deltanet.models.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RegistroProyectoId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id_cronograma")
    private int idCronograma;

    @Column(name = "id_proyecto")
    private int idProyecto;

    // Constructor vacío
    public RegistroProyectoId() {
    }

    // Constructor con parámetros
    public RegistroProyectoId(int idCronograma, int idProyecto) {
        this.idCronograma = idCronograma;
        this.idProyecto = idProyecto;
    }

    // Getters y setters
    public int getIdCronograma() {
        return idCronograma;
    }

    public void setIdCronograma(int idCronograma) {
        this.idCronograma = idCronograma;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    // Métodos equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroProyectoId that = (RegistroProyectoId) o;
        return idCronograma == that.idCronograma && idProyecto == that.idProyecto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCronograma, idProyecto);
    }
}
