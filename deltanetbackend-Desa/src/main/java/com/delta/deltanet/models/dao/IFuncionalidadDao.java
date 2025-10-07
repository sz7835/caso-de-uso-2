package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Funcionalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFuncionalidadDao extends JpaRepository<Funcionalidad, Long> {
    @Query("select a.iD_FUNCIONALIDAD, a.noM_FUNCIONALIDAD from Funcionalidad a where a.estado = 1")
    List<Object> listaFuncionalidades();

    // Buscar el padre por route exacto
    @Query("SELECT f FROM Funcionalidad f WHERE f.route = :route")
    Funcionalidad findByRoute(String route);

    // Buscar hijos por ID_FUNCIONALIDAD_PADRE
    @Query("SELECT f FROM Funcionalidad f WHERE f.iD_FUNCIONALIDAD_PADRE = :idPadre")
    List<Funcionalidad> findByPadreId(Long idPadre);
}
