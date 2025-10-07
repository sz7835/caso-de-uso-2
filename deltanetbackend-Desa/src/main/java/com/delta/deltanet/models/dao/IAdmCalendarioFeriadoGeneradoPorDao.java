package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmCalendarioFeriadoGeneradoPor;

@Repository
public interface IAdmCalendarioFeriadoGeneradoPorDao extends JpaRepository<AdmCalendarioFeriadoGeneradoPor, Long> {
    Optional<AdmCalendarioFeriadoGeneradoPor> findById(Long id);
    @Query("SELECT c FROM AdmCalendarioFeriadoGeneradoPor c WHERE LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :descripcion, '%'))")
    List<AdmCalendarioFeriadoGeneradoPor> findByDescripcion(@Param("descripcion") String descripcion);
    List<AdmCalendarioFeriadoGeneradoPor> findByEstado(Integer estado);
    @Query("SELECT c FROM AdmCalendarioFeriadoGeneradoPor c WHERE (LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :descripcion, '%'))) AND c.estado = :estado")
    List<AdmCalendarioFeriadoGeneradoPor> findByDescripcionAndEstado(@Param("descripcion") String descripcion, @Param("estado") Integer estado);
    boolean existsByDescripcionIgnoreCase(String descripcion);
    boolean existsByAcronimoIgnoreCase(String acronimo);
}