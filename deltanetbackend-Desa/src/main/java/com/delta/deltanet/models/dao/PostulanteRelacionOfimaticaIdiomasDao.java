package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.PostulanteRelacionOfimaticaIdiomas;

public interface PostulanteRelacionOfimaticaIdiomasDao extends JpaRepository<PostulanteRelacionOfimaticaIdiomas, Long> {

    List<PostulanteRelacionOfimaticaIdiomas> findByPostulanteDataId(Integer postulanteDataId);

    @Query("SELECT r FROM PostulanteRelacionOfimaticaIdiomas r JOIN FETCH r.ofimaticaIdiomas WHERE r.postulanteDataId = :postulanteDataId")
    List<PostulanteRelacionOfimaticaIdiomas> findByPostulanteDataIdWithOfimaticaIdiomas(
            @Param("postulanteDataId") Integer postulanteDataId);

    @Query("from PostulanteRelacionOfimaticaIdiomas a where a.postulanteDataId = ?1 and a.id = ?2")
    Optional<PostulanteRelacionOfimaticaIdiomas> busca(Integer postulanteDataId, Long idConocimiento);
}