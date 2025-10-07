package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmDatosTrabajador;

@Repository
public interface AdmDatosTrabajadorDao extends JpaRepository<AdmDatosTrabajador, Long> {

    @Query("SELECT t FROM AdmDatosTrabajador t WHERE t.personaId = :personaId")
    AdmDatosTrabajador findByPersonaId(@Param("personaId") Long personaId);

}
