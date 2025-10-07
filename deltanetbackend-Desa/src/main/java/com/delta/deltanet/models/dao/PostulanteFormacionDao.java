package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.PostulanteFormacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostulanteFormacionDao extends JpaRepository<PostulanteFormacion, Long> {

    @Query("SELECT COUNT(p) FROM PostulanteFormacion p WHERE LOWER(TRIM(p.nombre)) = LOWER(TRIM(:nombre)) AND p.estado = 1 AND (:exceptId IS NULL OR p.id <> :exceptId)")
    long countByNombreActivo(@Param("nombre") String nombre, @Param("exceptId") Long exceptId);

}
