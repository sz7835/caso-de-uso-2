package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Discapacidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDiscapacidadDao extends JpaRepository<Discapacidad,Integer> {
    @Query("from Discapacidad where estado = 1")
    List<Discapacidad> listaActivas();

    @Query("from Discapacidad where estado = 1 and codTipo = ?1")
    List<Discapacidad> listaByTipo(Integer idTipo);
}
