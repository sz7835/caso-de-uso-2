package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.PerNatSexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPerNatSexoDao extends JpaRepository<PerNatSexo, Integer> {
}
