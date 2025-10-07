package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TrabajadorSalud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ITrabajadorSaludDao extends JpaRepository<TrabajadorSalud, Long> {
    @Query("from TrabajadorSalud where idPerNat = ?1")
    public TrabajadorSalud buscaByPerNat(Long idPerNat);

    @Query("SELECT s FROM TrabajadorSalud s WHERE s.idPerNat = :personaId")
    List<TrabajadorSalud> findByPersonaNaturalId(@Param("personaId") Long personaId);

}
