package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.ContratoPerfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContratoPerfilServiceDao extends JpaRepository<ContratoPerfiles, Long> {
    @Query("from ContratoPerfiles a where a.idContrato = ?1 and a.idPerfil = ?2")
    Optional<ContratoPerfiles> busca(Long idContrato, Long idPerfil);

    @Query("from ContratoPerfiles a where a.idContrato = ?1")
    List<ContratoPerfiles> getPerfiles(Long idContrato);
}
