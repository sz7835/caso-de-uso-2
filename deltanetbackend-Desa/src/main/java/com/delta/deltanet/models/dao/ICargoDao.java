package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.Cargo;

public interface ICargoDao extends JpaRepository<Cargo, Long> {
}
