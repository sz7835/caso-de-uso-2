package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.TipoDoc;

public interface ITipoDocDao extends JpaRepository<TipoDoc, Long> {
}
