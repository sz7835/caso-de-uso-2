package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.Persona;

public interface IPersonaDao2 extends JpaRepository<Persona,Long> {
    
    @Query("SELECT p FROM Persona p WHERE p.tipoPer.id = :idTipoPersona")
    List<Persona> findByTipoPerId(@Param("idTipoPersona") Long idTipoPersona);
}
