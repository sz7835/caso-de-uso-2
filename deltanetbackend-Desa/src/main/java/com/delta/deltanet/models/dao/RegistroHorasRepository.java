package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.OutRegistroHoras;

public interface RegistroHorasRepository extends JpaRepository<OutRegistroHoras, Integer> {

    List<OutRegistroHoras> findAllByOrderByDiaDesc();

    List<OutRegistroHoras> findByDiaBetweenOrderByDiaDesc(Date fechaInicio, Date fechaFin);

    Optional<OutRegistroHoras> findById(int id);
}