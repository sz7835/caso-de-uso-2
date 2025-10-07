package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.Actividad;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    List<Actividad> findByTipoActividad_IdAndPerPersonaIdAndFecha(int tipoActividadId, int perPersonaId, Date fecha);

    @Query(value = "SELECT * FROM out_registro_actividad " +
            "WHERE per_persona_id = :personaId " +
            "AND fecha = :fechaAhora " +
            "AND create_date < now() " +
            "ORDER BY create_date DESC LIMIT 1", nativeQuery = true)
    Optional<Actividad> findUltimaActividadAntesDe(@Param("personaId") int personaId, @Param("fechaAhora") String fechaAhora);

}