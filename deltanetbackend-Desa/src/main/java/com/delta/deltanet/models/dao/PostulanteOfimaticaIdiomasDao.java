package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.PostulanteOfimaticaIdiomas;

public interface PostulanteOfimaticaIdiomasDao extends JpaRepository<PostulanteOfimaticaIdiomas, Long> {

    @Query("SELECT COUNT(p) FROM PostulanteOfimaticaIdiomas p WHERE LOWER(TRIM(p.descripcion)) = LOWER(TRIM(:descripcion)) AND p.estado = 1" )
    Long countByDescripcionActivo(@Param("descripcion") String descripcion);

    @Query("SELECT p FROM PostulanteOfimaticaIdiomas p " +
            "WHERE (:descripcion IS NULL OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) " +
            "AND (:estado = 0 OR p.estado = :estado)")
    List<PostulanteOfimaticaIdiomas> findByDescripcionAndEstado(
            @Param("descripcion") String descripcion,
            @Param("estado") Integer estado);

    @Query("from PostulanteOfimaticaIdiomas c where c.id = ?1")
    PostulanteOfimaticaIdiomas buscaId(Long idPer);

}
