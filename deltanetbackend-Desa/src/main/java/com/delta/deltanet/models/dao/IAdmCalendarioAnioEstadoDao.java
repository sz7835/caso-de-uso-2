package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;

@Repository
public interface IAdmCalendarioAnioEstadoDao extends JpaRepository<AdmCalendarioAnioEstado, Long> {

    Optional<AdmCalendarioAnioEstado> findById(Long id);
    @Query("SELECT c FROM AdmCalendarioAnioEstado c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<AdmCalendarioAnioEstado> findByNombre(@Param("nombre") String nombre);
    List<AdmCalendarioAnioEstado> findByEstado(Integer estado);
    @Query("SELECT c FROM AdmCalendarioAnioEstado c WHERE (LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND c.estado = :estado")
    List<AdmCalendarioAnioEstado> findByNombreAndEstado(@Param("nombre") String nombre, @Param("estado") Integer estado);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByAcronimoIgnoreCase(String acronimo);
    @Query("SELECT COUNT(c) FROM AdmCalendarioAnioEstado c WHERE c.estado = 1 AND ((c.idAnioEstado = 1 AND LOWER(TRIM(c.nombre)) = 'actual') OR (c.idAnioEstado = 2 AND LOWER(TRIM(c.nombre)) = 'pasado') OR (c.idAnioEstado = 3 AND LOWER(TRIM(c.nombre)) = 'futuro'))")
    long countPrincipalActivo();

    @Query("SELECT COUNT(c) > 0 FROM AdmCalendarioAnioEstado c WHERE c.estado = 1 AND c.idAnioEstado = 1 AND LOWER(TRIM(c.nombre)) = 'actual'")
    boolean existsActualActivo();

    @Query("SELECT COUNT(c) > 0 FROM AdmCalendarioAnioEstado c WHERE c.estado = 1 AND c.idAnioEstado = 2 AND LOWER(TRIM(c.nombre)) = 'pasado'")
    boolean existsPasadoActivo();

    @Query("SELECT COUNT(c) > 0 FROM AdmCalendarioAnioEstado c WHERE c.estado = 1 AND c.idAnioEstado = 3 AND LOWER(TRIM(c.nombre)) = 'futuro'")
    boolean existsFuturoActivo();
}