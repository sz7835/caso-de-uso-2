package com.delta.deltanet.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "out_tipo_actividad")
public class OutTipoActividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45)
    private String nombre;
}
