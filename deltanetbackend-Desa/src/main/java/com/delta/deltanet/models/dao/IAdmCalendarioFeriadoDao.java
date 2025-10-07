package com.delta.deltanet.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.entity.AdmCalendarioFeriado;

import java.util.Date;
import java.util.List;

@Repository
public interface IAdmCalendarioFeriadoDao extends JpaRepository<AdmCalendarioFeriado, Long> {

	@Query("SELECT r FROM AdmCalendarioFeriado r WHERE r.motivo = :motivo AND r.idAnio = :idAnio AND r.idFeriadoGeneradoPor = :idGeneradoPor AND r.aplicableSectorPublico = :aplicableSectorPublico AND r.aplicableSectorPrivado = :aplicableSectorPrivado AND r.fecha = :fecha AND r.idSemanaDia = :idDiaSemana")
	List<AdmCalendarioFeriado> findByMotivoAndCamposClave(@Param("motivo") String motivo,
														  @Param("idAnio") Long idAnio,
														  @Param("idGeneradoPor") Long idGeneradoPor,
														  @Param("aplicableSectorPublico") String aplicableSectorPublico,
														  @Param("aplicableSectorPrivado") String aplicableSectorPrivado,
														  @Param("fecha") Date fecha,
														  @Param("idDiaSemana") Long idDiaSemana);

	@Query("SELECT r FROM AdmCalendarioFeriado r WHERE r.idCalendarioFeriado = ?1")
	public AdmCalendarioFeriado find(Long id);

	@Query("SELECT r FROM AdmCalendarioFeriado r WHERE (:motivo IS NULL OR LOWER(r.motivo) LIKE LOWER(CONCAT('%', :motivo, '%'))) AND (:generado IS NULL OR r.idFeriadoGeneradoPor = :generado) AND (:anio IS NULL OR r.idAnio = :anio) AND (:estado IS NULL OR r.estado = :estado) order by r.fecha asc")
	public List<AdmCalendarioFeriado> findBySearch(@Param("motivo") String motivo, @Param("generado") Long generado,
			@Param("anio") Long anio, @Param("estado") Long estado);

	@Query("SELECT r FROM AdmCalendarioFeriado r WHERE (:motivo IS NULL OR LOWER(r.motivo) LIKE LOWER(CONCAT('%', :motivo, '%'))) AND (:generado IS NULL OR r.idFeriadoGeneradoPor = :generado) AND (:anio IS NULL OR r.idAnio = :anio) AND (:estado IS NULL OR r.estado = :estado) order by r.fecha asc")
	public Page<AdmCalendarioFeriado> findBySearchPaginated(@Param("motivo") String motivo,
			@Param("generado") Long generado, @Param("anio") Long anio, @Param("estado") Long estado,
			Pageable pageable);

	@Query("SELECT c FROM AdmCalendarioFeriado c WHERE c.idAnio = ?1")
	public List<AdmCalendarioFeriado> findYear(Long idAnio);

	@Query("SELECT r FROM AdmCalendarioFeriado r WHERE r.fecha = ?1")
	public List<AdmCalendarioFeriado> findFecha(Date fecha);

	@Transactional
    @Modifying
	@Query("DELETE FROM AdmCalendarioFeriado c WHERE c.idAnio = ?1")
	public void removeAll(Long idAnio);

	
}