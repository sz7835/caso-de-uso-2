package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.PostulanteRelacionRequisitos;

public interface PostulanteRelacionRequisitosDao extends JpaRepository<PostulanteRelacionRequisitos, Long> {

    @Query("SELECT r FROM PostulanteRelacionRequisitos r JOIN FETCH r.requisito WHERE r.postulanteDataId = :postulanteDataId")
    List<PostulanteRelacionRequisitos> findByPostulanteDataIdWithRequisito(
            @Param("postulanteDataId") Integer postulanteDataId);

    @Query("from PostulanteRelacionRequisitos a where a.postulanteDataId = ?1 and a.id = ?2")
    Optional<PostulanteRelacionRequisitos> busca(Integer idPostulante, Long idRequisito);
}
