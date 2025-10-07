package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmCalendarioSemanaDia;

@Repository
public interface IAdmCalendarioSemanaDiaDao extends JpaRepository<AdmCalendarioSemanaDia, Long> {
    Optional<AdmCalendarioSemanaDia> findById(Long id);
    @Query("SELECT c FROM AdmCalendarioSemanaDia c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<AdmCalendarioSemanaDia> findByNombre(@Param("nombre") String nombre);
    List<AdmCalendarioSemanaDia> findByEstado(Integer estado);
    @Query("SELECT c FROM AdmCalendarioSemanaDia c WHERE (LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND c.estado = :estado")
    List<AdmCalendarioSemanaDia> findByNombreAndEstado(@Param("nombre") String nombre, @Param("estado") Integer estado);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByAcronimoIgnoreCase(String acronimo);
}