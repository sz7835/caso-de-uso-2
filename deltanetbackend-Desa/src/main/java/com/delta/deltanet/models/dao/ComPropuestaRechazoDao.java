package com.delta.deltanet.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.ComPropuestaMotivoRechazo;

@Repository
public interface ComPropuestaRechazoDao extends JpaRepository<ComPropuestaMotivoRechazo, Long> {
    Optional<ComPropuestaMotivoRechazo> findByComPropuesta(Long comPropuesta);
}