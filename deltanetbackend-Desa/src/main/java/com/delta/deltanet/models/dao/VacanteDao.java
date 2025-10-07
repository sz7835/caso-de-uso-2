package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.Vacante;

public interface VacanteDao extends JpaRepository<Vacante, Long> {
}