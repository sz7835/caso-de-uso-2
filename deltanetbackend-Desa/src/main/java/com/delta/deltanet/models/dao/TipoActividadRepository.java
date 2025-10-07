package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.TipoActividad;

@Repository
public interface TipoActividadRepository extends JpaRepository<TipoActividad, Long> {
    @Query("SELECT COUNT(t) FROM TipoActividad t WHERE LOWER(TRIM(t.nombre)) = LOWER(TRIM(:nombre)) AND t.estado = 1 AND (:exceptId IS NULL OR t.id <> :exceptId)")
    long countByNombreActivo(@Param("nombre") String nombre, @Param("exceptId") Long exceptId);
}
