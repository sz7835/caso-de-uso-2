package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.ContratoContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IContratoContactoDao extends JpaRepository<ContratoContacto, Long> {
    @Query("from ContratoContacto a where a.idContrato = ?1 and a.idContacto = ?2")
    Optional<ContratoContacto> busca(Long idContrato,Long idContacto);

    @Query("from ContratoContacto a where a.idContrato = ?1")
    List<ContratoContacto> getContactos(Long idContrato);
}
