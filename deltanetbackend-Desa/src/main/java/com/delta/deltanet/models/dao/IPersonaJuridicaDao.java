package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.PersonaJuridica;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPersonaJuridicaDao extends JpaRepository<PersonaJuridica, Long> {

    @Query("SELECT p.idPerJur FROM PersonaJuridica p WHERE p.estado = 1")
    Optional<Long> findContactoIdByIdConsultor();

    @Query("select u.idPerJur, u.razonSocial FROM PersonaJuridica u WHERE u.tipo = 'C' and u.estado = 1")
    List<Object> listaClientes();
}
