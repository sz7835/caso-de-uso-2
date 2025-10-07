package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.PersonaNatural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IPersonaNaturalDao extends JpaRepository<PersonaNatural, Long> {

    @Query("SELECT p FROM PersonaNatural p WHERE p.id = :personaId")
    PersonaNatural findByPersonaNaturalId(@Param("personaId") Long personaId);
}
