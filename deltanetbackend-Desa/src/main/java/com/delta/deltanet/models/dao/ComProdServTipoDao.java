package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.ComProdServTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComProdServTipoDao extends JpaRepository<ComProdServTipo, Integer> {
}
