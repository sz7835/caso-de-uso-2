package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AdmCalendarioMes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAdmCalendarioMesDao extends JpaRepository<AdmCalendarioMes, Long> {
    Optional<AdmCalendarioMes> findById(Long id);
    @Query("SELECT c FROM AdmCalendarioMes c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<AdmCalendarioMes> findByNombre(@Param("nombre") String nombre);
    List<AdmCalendarioMes> findByEstado(Integer estado);
    @Query("SELECT c FROM AdmCalendarioMes c WHERE (LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(c.acronimo) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND c.estado = :estado")
    List<AdmCalendarioMes> findByNombreAndEstado(@Param("nombre") String nombre, @Param("estado") Integer estado);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByAcronimoIgnoreCase(String acronimo);
}