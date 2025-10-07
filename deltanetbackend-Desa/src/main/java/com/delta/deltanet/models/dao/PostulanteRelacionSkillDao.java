package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.PostulanteRelacionSkill;

public interface PostulanteRelacionSkillDao extends JpaRepository<PostulanteRelacionSkill, Long> {

    List<PostulanteRelacionSkill> findByPostulanteDataId(Integer postulanteDataId);

    @Query("SELECT r FROM PostulanteRelacionSkill r JOIN FETCH r.skill WHERE r.postulanteDataId = :postulanteDataId")
    List<PostulanteRelacionSkill> findByPostulanteDataIdWitSkill(
            @Param("postulanteDataId") Integer postulanteDataId);

    @Query("from PostulanteRelacionSkill a where a.postulanteDataId = ?1 and a.id = ?2")
    Optional<PostulanteRelacionSkill> busca(Integer postulanteDataId, Long skillId);
}
