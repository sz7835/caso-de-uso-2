package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmCalendarioFeriadoHistorico;

import java.util.Date;
import java.util.List;

@Repository
public interface IAdmCalendarioFeriadoHistoricoDao extends JpaRepository<AdmCalendarioFeriadoHistorico, Long> {

	  @Query("SELECT r FROM AdmCalendarioFeriadoHistorico r WHERE r.idCalendarioFeriado = ?1")
	  AdmCalendarioFeriadoHistorico find(Long id);

	  @Query("SELECT r FROM AdmCalendarioFeriadoHistorico r " +
		       "JOIN r.calendarioAnio a " +
		       "JOIN a.anioEstado ae " +
		       "WHERE (:motivo IS NULL OR LOWER(r.motivo) LIKE LOWER(CONCAT('%', :motivo, '%'))) " +
		       "AND (:generado IS NULL OR r.idFeriadoGeneradoPor = :generado) " +
		       "AND (:anio IS NULL OR r.idAnio = :anio) " +
		       "AND (:estado IS NULL OR r.estado = :estado) " +
		       "AND ae.idAnioEstado = 1 " +
		       "ORDER BY r.fecha ASC")
	  public List<AdmCalendarioFeriadoHistorico> findBySearch(
			  @Param("motivo") String motivo,
			  @Param("generado") Long generado,
			  @Param("anio") Long anio,
			  @Param("estado") Long estado);


		@Query("SELECT r FROM AdmCalendarioFeriadoHistorico r WHERE r.fecha = ?1")
		public List<AdmCalendarioFeriadoHistorico> findFecha(Date fecha);
}