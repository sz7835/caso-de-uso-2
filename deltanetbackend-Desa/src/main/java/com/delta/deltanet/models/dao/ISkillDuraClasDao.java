package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SkillDuraClas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISkillDuraClasDao extends JpaRepository<SkillDuraClas,Long> {
    List<SkillDuraClas> findByDescripcionAndEstado(String descripcion, Integer estado);
}
