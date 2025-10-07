package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Calendario;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ICalendarioDao extends JpaRepository<Calendario, Long> {

	@Query("SELECT COUNT(c) FROM Calendario c WHERE c.fecha = :fecha AND c.estado = 1 AND (:exceptId IS NULL OR c.id <> :exceptId)")
	long countByFechaAndEstadoActivo(@Param("fecha") Date fecha, @Param("exceptId") Long exceptId);
	@Query("SELECT COUNT(c) FROM Calendario c WHERE c.idAnio = :idAnio AND c.idMes = :idMes AND c.idSemanaDia = :idSemanaDia AND c.idSemanaDiaTipo = :idSemanaDiaTipo AND c.contadorDia = :contadorDia AND c.fecha = :fecha AND ((:idFeriado IS NULL AND c.idFeriado IS NULL) OR c.idFeriado = :idFeriado) AND c.estado = 1 AND (:exceptId IS NULL OR c.id <> :exceptId)")
	long countByCamposUnicos(@Param("idAnio") Long idAnio, @Param("idMes") Long idMes, @Param("idSemanaDia") Long idSemanaDia, @Param("idSemanaDiaTipo") Long idSemanaDiaTipo, @Param("contadorDia") Long contadorDia, @Param("fecha") Date fecha, @Param("idFeriado") Long idFeriado, @Param("exceptId") Long exceptId);

	@Query("SELECT r FROM Calendario r WHERE r.id = ?1")
	Calendario find(Long id);

	@Query("select c " + "from Calendario c "
			+ "where c.idSemanaDiaTipo = 1 and c.idFeriado is null and c.fecha <= ?1 order by c.fecha desc")
	public List<Calendario> buscaRangoFecha(Date fecIni, Pageable pageable);

	@Query("select c " + "from Calendario c " + "where c.fecha = ?1")
	public Calendario findDate(Date fecIni);

	@Query("SELECT c FROM Calendario c WHERE (:fecha IS NULL OR c.fecha = :fecha) AND (:idMes IS NULL OR c.idMes = :idMes) AND (:idSemanaDiaTipo IS NULL OR c.idSemanaDiaTipo = :idSemanaDiaTipo) AND (:estado IS NULL OR c.estado = :estado) order by c.fecha asc")
	public List<Calendario> findBySearch(@Param("fecha") Date fecha, @Param("idMes") Long idMes, @Param("idSemanaDiaTipo") Long idSemanaDiaTipo, @Param("estado") Long estado);

	@Query("SELECT c FROM Calendario c WHERE c.idAnio = ?1")
	public List<Calendario> findYear(Long idAnio);

	@Transactional
    @Modifying
	@Query("DELETE FROM Calendario c WHERE c.idAnio = ?1")
	public void removeAll(Long idAnio);

}
