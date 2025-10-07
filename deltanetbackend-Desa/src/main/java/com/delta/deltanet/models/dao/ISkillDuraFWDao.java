package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SkillDuraFrameWork;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISkillDuraFWDao extends JpaRepository<SkillDuraFrameWork,Long> {
	List<SkillDuraFrameWork> findByDescripcionAndEstado(String descripcion, Integer estado);
}
