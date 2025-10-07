package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.AdmNivel;

public interface AdmNivelDao extends JpaRepository<AdmNivel, Integer> {
  @Query("SELECT COUNT(r) FROM AdmNivel r WHERE LOWER(r.descripcion) = LOWER(:descripcion) AND r.estado = 1 AND (:idExcluir IS NULL OR r.id <> :idExcluir)")
  int countByDescripcionActivoExcluyendoId(@Param("descripcion") String descripcion, @Param("idExcluir") Integer idExcluir);

  @Query("SELECT r FROM AdmNivel r WHERE (:descripcion IS NULL OR LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) AND (:estado = 0 OR r.estado = :estado)")
  List<AdmNivel> findByDescripcionAndEstado(@Param("descripcion") String descripcion, @Param("estado") int estado);

  @Query("SELECT r FROM AdmNivel r WHERE (:descripcion IS NULL OR LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) AND (:estado = 0 OR r.estado = :estado)")
  Page<AdmNivel> findByDescripcionAndEstadoPaginated(@Param("descripcion") String descripcion,
      @Param("estado") int estado,
      Pageable pageable);

}