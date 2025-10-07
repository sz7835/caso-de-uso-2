package com.delta.deltanet.models.dao;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.RegistroProyecto;

public interface RegistroProyectoRepository extends JpaRepository<RegistroProyecto, Long> {

    @Query("SELECT r FROM RegistroProyecto r WHERE r.idPersona = ?1")
    List<RegistroProyecto> findByCronogramaIds(Long idPersona);

    @Query("SELECT rp FROM RegistroProyecto rp WHERE rp.codigo = ?1")
    Optional<RegistroProyecto> findByCodigo(String codigo);
}