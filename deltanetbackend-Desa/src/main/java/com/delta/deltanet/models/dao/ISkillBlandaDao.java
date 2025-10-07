package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SkillBlanda;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISkillBlandaDao extends JpaRepository<SkillBlanda,Long> {
    List<SkillBlanda> findByDescripcionAndIdClasAndEstado(String descripcion, Long idClas, Integer estado);
}
