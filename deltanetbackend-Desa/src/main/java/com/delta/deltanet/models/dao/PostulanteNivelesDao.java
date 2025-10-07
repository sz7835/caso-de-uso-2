package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.PostulanteNiveles;

public interface PostulanteNivelesDao extends JpaRepository<PostulanteNiveles, Long> {

    @Query("SELECT COUNT(p) FROM PostulanteNiveles p WHERE LOWER(TRIM(p.descripcion)) = LOWER(TRIM(:descripcion)) AND p.estado = 1" )
    Long countByDescripcionActivo(@org.springframework.data.repository.query.Param("descripcion") String descripcion);

    @Query("from PostulanteNiveles c where c.id = ?1")
    PostulanteNiveles buscaId(Long idPer);
}
