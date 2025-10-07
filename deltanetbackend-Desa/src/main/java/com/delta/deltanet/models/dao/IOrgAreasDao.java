package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.OrgAreas;

public interface IOrgAreasDao extends JpaRepository<OrgAreas, Long>{

    @Query("SELECT a.gerencia.id FROM OrgAreas a WHERE a.id = :idArea")
    Optional<Long> findGerenciaIdByAreaId(@Param("idArea") Long idArea);

    @Query("SELECT nombre FROM OrgAreas WHERE id = :idArea")
    String findAreaNameById(@Param("idArea") Long idArea);

    @Query("from OrgAreas where estado = 1 order by nombre asc")
	public List<OrgAreas> findAll();
}
