package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PersonaJuridica;

import java.util.List;
import java.util.Optional;

public interface IPersonaJuridicaService {
    PersonaJuridica save(PersonaJuridica personaJuridica);
    Optional<PersonaJuridica> findById(Long id);
    List<Object> listaClientes();
}
