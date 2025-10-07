package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.PostulanteData;

public interface PostulanteDataDao extends JpaRepository<PostulanteData, Long> {
    @Query("SELECT p FROM PostulanteData p " +
            "WHERE (:nombre IS NULL OR CONCAT(p.nombre, ' ', p.apellidoPat, ' ', p.apellidoMat) LIKE %:nombre%) " +
            "AND (:formacionAcademica IS NULL OR :formacionAcademica = 0 OR p.formacionAcademica = :formacionAcademica) "
            +
            "AND (:gradoAcademico IS NULL OR :gradoAcademico = 0 OR p.gradoAcademico = :gradoAcademico) " +
            "AND (:estado IS NULL OR :estado = 0 OR p.estado = :estado)")
    List<PostulanteData> searchPostulantes(
            @Param("nombre") String nombre,
            @Param("formacionAcademica") Integer formacionAcademica,
            @Param("gradoAcademico") Integer gradoAcademico,
            @Param("estado") Integer estado);

}