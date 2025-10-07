package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.delta.deltanet.models.entity.PostulanteSkill;

public interface PostulanteSkillDao extends JpaRepository<PostulanteSkill, Long> {
    @Query("SELECT COUNT(p) FROM PostulanteSkill p WHERE LOWER(TRIM(p.descripcion)) = LOWER(TRIM(:descripcion)) AND p.estado = 1 AND (:exceptId IS NULL OR p.id <> :exceptId)")
    long countByDescripcionActivo(@Param("descripcion") String descripcion, @Param("exceptId") Long exceptId);

    @Query("SELECT p FROM PostulanteSkill p " +
            "WHERE (:descripcion IS NULL OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) " +
            "AND (:estado = 0 OR p.estado = :estado)")
    List<PostulanteSkill> findByNombreAndEstado(@Param("descripcion") String descripcion,
                                               @Param("estado") Integer estado);

    @Query("from PostulanteSkill c where c.id = ?1")
    PostulanteSkill buscaId(Long idPer);
}
