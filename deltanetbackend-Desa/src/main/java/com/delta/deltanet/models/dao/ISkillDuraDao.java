package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SkillDura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISkillDuraDao extends JpaRepository<SkillDura,Long> {
    List<SkillDura> findByDescripcionAndIdClasAndEstado(String descripcion, Long idClas, Integer estado);
}
