package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TrabajadorFamilia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITrabFamiliaDao extends JpaRepository<TrabajadorFamilia, Long> {

    @Query("select a.id, pn.nombre, pn.apePaterno, pn.apeMaterno, a.convivencia " +
        "from TrabajadorFamilia a " +
        "inner join a.perNat pn " +
        "where a.idPerNat = ?1")
    public List<Object> listaFamilia(Long idPerNat);

    @Query("SELECT f FROM TrabajadorFamilia f WHERE f.idPerNat = :personaId")
    List<TrabajadorFamilia> findByPersonaNaturalId(@Param("personaId") Long personaId);

    @Query("SELECT tf FROM TrabajadorFamilia tf WHERE tf.idPerNat = :personaId")
    List<TrabajadorFamilia> findByPersonaId(@Param("personaId") Long personaId);
}
