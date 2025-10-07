package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AdmCronograma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdmCronogramaRepository extends JpaRepository<AdmCronograma, Integer> {

    List<AdmCronograma> findByIdConsultor(int idConsultor);

    List<AdmCronograma> findByIdConsultorAndEstado(Integer idConsultor, int estado);

    @Query("SELECT a.id FROM AdmCronograma a WHERE a.idConsultor = :idConsultor")
    List<Integer> findIdsByIdConsultor(int idConsultor);

    List<AdmCronograma> findByIdAndIdConsultor(int idCronograma, int idConsultor);

    List<AdmCronograma> findByIdOrIdConsultor(int id, int idConsultor);

    @Query("SELECT c FROM AdmCronograma c WHERE c.id = :idCronograma")
    List<AdmCronograma> findByIdCronograma(@Param("idCronograma") int id);

    @Query("SELECT c.idContrato FROM AdmCronograma c WHERE c.id = :idCronograma")
    Optional<Long> findIdContratoByIdCronograma(@Param("idCronograma") Long idCronograma);

}