package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.ClienteSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ClienteSolicitudDao extends JpaRepository<ClienteSolicitud, Integer> {

        @Query("SELECT cs FROM ClienteSolicitud cs WHERE "
                        + "(:id = 0 OR cs.id = :id) AND "
                        + "(:descripcion IS NULL OR LOWER(cs.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) AND "
                        + "(:fechaInicio IS NULL OR cs.fechaCompromiso >= :fechaInicio) AND "
                        + "(:fechaFin IS NULL OR cs.fechaCompromiso <= :fechaFin) AND "
                        + "(:estado = 0 OR cs.estado = :estado)")
        List<ClienteSolicitud> findWithFilters(
                        @Param("id") Integer id,
                        @Param("descripcion") String descripcion,
                        @Param("fechaInicio") Date fechaInicio,
                        @Param("fechaFin") Date fechaFin,
                        @Param("estado") Integer estado);

}
