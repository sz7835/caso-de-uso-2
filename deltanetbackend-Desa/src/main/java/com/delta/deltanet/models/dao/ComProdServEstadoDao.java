package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.ComProdServEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComProdServEstadoDao extends JpaRepository<ComProdServEstado, Integer> {
}
