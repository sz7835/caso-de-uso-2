package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Puestos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPuestoDao extends JpaRepository<Puestos,Long> {
}
