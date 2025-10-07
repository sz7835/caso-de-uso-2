package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.PersonaCliente;

@SuppressWarnings("all")
public interface IPersonaClienteDao extends JpaRepository<PersonaCliente, Long> {

    @Query("from PersonaCliente where idPersona = ?1 and estado = ?2")
    List<PersonaCliente> findAllPS(Long idPersona, Long estado);

    @Query("from PersonaCliente where idPersona = ?1")
    List<PersonaCliente> findAllP(Long idPersona);

    @Query("from PersonaCliente where idPersona = ?1 and idCliente = ?2 and estado = 1")
    PersonaCliente findPersonaAndClient(Long idPersona, Long idCliente);
}
