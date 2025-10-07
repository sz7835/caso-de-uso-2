package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITipoPersonaDao extends JpaRepository<TipoPersona, Long> {
    @Query("from TipoPersona where idTipoPer = ?1")
    TipoPersona buscaTipoPer(Long idTipoPer);
}
