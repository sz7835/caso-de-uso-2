package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmTipoDiaSemana;

@Repository
public interface IAdmTipoDiaSemanaDao extends JpaRepository<AdmTipoDiaSemana, Long> {
    Optional<AdmTipoDiaSemana> findById(Long id);
    @Query("SELECT c FROM AdmTipoDiaSemana c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<AdmTipoDiaSemana> findByNombre(@Param("nombre") String nombre);
    List<AdmTipoDiaSemana> findByEstado(Integer estado);
    @Query("SELECT c FROM AdmTipoDiaSemana c WHERE (LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND c.estado = :estado")
    List<AdmTipoDiaSemana> findByNombreAndEstado(@Param("nombre") String nombre, @Param("estado") Integer estado);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByAcronimoIgnoreCase(String acronimo);
}