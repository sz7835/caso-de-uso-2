package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.ComProductoServicio;

public interface ComProductoServicioDao extends JpaRepository<ComProductoServicio, Integer> {
    @Query("SELECT COUNT(p) FROM ComProductoServicio p WHERE LOWER(p.descripcion) = LOWER(:descripcion) AND p.idTipo = :idTipo AND p.estado = 1 AND (:idExcluir IS NULL OR p.id <> :idExcluir)")
    int countByDescripcionAndIdTipoAndActivoExcluyendoId(String descripcion, Integer idTipo, Integer idExcluir);

  @Query("SELECT p FROM ComProductoServicio p WHERE (:idTipo = 0 OR p.idTipo = :idTipo) AND (:estado = 0 OR p.estado = :estado) AND (:descripcion IS NULL OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))")
  List<ComProductoServicio> findByParameters(@Param("idTipo") int idTipo, @Param("estado") int estado,
      @Param("descripcion") String descripcion);

  @Query("SELECT p FROM ComProductoServicio p WHERE (:idTipo = 0 OR p.idTipo = :idTipo) AND (:estado = 0 OR p.estado = :estado) AND (:descripcion IS NULL OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))")
  Page<ComProductoServicio> findByParametersPaginated(@Param("idTipo") int idTipo,
      @Param("estado") int estado, @Param("descripcion") String descripcion, Pageable pageable);

}