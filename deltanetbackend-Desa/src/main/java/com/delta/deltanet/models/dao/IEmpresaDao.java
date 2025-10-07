package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.Empresa;

public interface IEmpresaDao extends JpaRepository<Empresa, Long> {
}
