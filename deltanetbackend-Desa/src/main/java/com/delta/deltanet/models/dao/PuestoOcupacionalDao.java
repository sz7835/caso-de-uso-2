package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.PuestoOcupacional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PuestoOcupacionalDao extends JpaRepository<PuestoOcupacional, Long> {

    PuestoOcupacional findByNombrePuestoIgnoreCaseAndUnidadOrganicaIgnoreCaseAndEstadoAndIdNot(String nombrePuesto, String unidadOrganica, Integer estado, Long id);

    PuestoOcupacional findByNombrePuestoIgnoreCaseAndUnidadOrganicaIgnoreCaseAndEstado(String nombrePuesto, String unidadOrganica, Integer estado);

    List<PuestoOcupacional> findById(Integer estado);

    @Query("SELECT p FROM PuestoOcupacional p " +
            "WHERE (:nombrePuesto IS NULL OR LOWER(p.nombrePuesto) LIKE LOWER(CONCAT('%', :nombrePuesto, '%'))) " +
            "AND (:estado IS NULL OR :estado = 0 OR p.estado = :estado)")
    List<PuestoOcupacional> filtrarPuestos(@Param("nombrePuesto") String nombrePuesto, @Param("estado") Integer estado);

}
