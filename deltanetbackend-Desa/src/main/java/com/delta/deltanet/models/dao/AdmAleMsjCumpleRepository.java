package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;

public interface AdmAleMsjCumpleRepository extends JpaRepository<AdmAleMsjCumple, Integer> {

    // Get all by genero/sexo
    List<AdmAleMsjCumple> findByIdSexo(Integer idSexo);

    // Latest by genero (used by GET /plantilla)
    Optional<AdmAleMsjCumple> findTopByIdSexoOrderByIdDesc(Integer idSexo);

    // Optional search by descripcion (for future UI filtering)
    @Query("SELECT a FROM AdmAleMsjCumple a " +
           "WHERE (:q IS NULL OR LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<AdmAleMsjCumple> searchByDescripcion(@Param("q") String q);
}
