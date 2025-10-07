package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import com.delta.deltanet.models.entity.ContratoPerfiles;

public interface ContratoPerfilService {
    Optional<ContratoPerfiles> busca(Long idContrato, Long idPerfil);

    ContratoPerfiles save(ContratoPerfiles contratoContacto);

    void delete(Long id);

    List<ContratoPerfiles> getPerfiles(Long idContrato);
}
