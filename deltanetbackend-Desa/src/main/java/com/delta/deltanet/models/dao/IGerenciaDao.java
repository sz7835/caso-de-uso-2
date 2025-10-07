package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Gerencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IGerenciaDao extends JpaRepository<Gerencia,Long> {

    @Query("SELECT DISTINCT g FROM Gerencia g JOIN FETCH g.orgAreas")
    List<Gerencia> findAllWithAreas();

    @Query("SELECT nombre FROM Gerencia WHERE id = :idGerencia")
    String findGerenciaNameById(@Param("idGerencia") Long idGerencia);
}
