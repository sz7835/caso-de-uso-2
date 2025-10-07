package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.ContratoOutsourcing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IContratoOutsourcingDao extends JpaRepository<ContratoOutsourcing,Long> {

    @Query("Select c " +
                    "from ContratoOutsourcing c " +
                    "inner join c.relacion r " +
                    "where r.idRel = ?1 and c.estado = 1")
    ContratoOutsourcing findRelation(Long idRel);


}
