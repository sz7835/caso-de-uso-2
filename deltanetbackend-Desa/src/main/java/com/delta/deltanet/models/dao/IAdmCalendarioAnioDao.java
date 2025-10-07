package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmCalendarioAnio;

import java.util.List;

@Repository
public interface IAdmCalendarioAnioDao extends JpaRepository<AdmCalendarioAnio, Integer> {
    List<AdmCalendarioAnio> findByNombreContainingIgnoreCase(String nombre);
    List<AdmCalendarioAnio> findByEstado(Integer estado);
    List<AdmCalendarioAnio> findByNombreContainingIgnoreCaseAndEstado(String nombre, Integer estado);
    List<AdmCalendarioAnio> findByAnioEstado_IdAnioEstado(Long idAnioEstado);
    List<AdmCalendarioAnio> findByAnioEstado_IdAnioEstadoAndEstado(Long idAnioEstado, Integer estado);
    List<AdmCalendarioAnio> findByNombreContainingIgnoreCaseAndAnioEstado_IdAnioEstado(String nombre, Long idAnioEstado);
    List<AdmCalendarioAnio> findByNombreContainingIgnoreCaseAndAnioEstado_IdAnioEstadoAndEstado(String nombre, Long idAnioEstado, Integer estado);
    
    boolean existsByNombreIgnoreCase(String nombre);

	  @Query("SELECT r FROM AdmCalendarioAnio r WHERE r.idCalendarioAnio = ?1")
	  AdmCalendarioAnio findByKey(Integer id);

	  @Query("SELECT r FROM AdmCalendarioAnio r "+
			  "join r.anioEstado a " +
			  "WHERE a.idAnioEstado = ?1")
	  AdmCalendarioAnio findByStatus(Long id);

}