package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.CalendarioHistorico;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ICalendarioHistoricoDao extends JpaRepository<CalendarioHistorico, Long> {

	  @Query("SELECT r FROM CalendarioHistorico r WHERE r.id = ?1")
	  CalendarioHistorico find(Long id);

    @Query("select c " +
            "from CalendarioHistorico c " +
            "where c.idSemanaDiaTipo = 1 and c.idFeriado is null and c.fecha <= ?1 order by c.fecha desc")
    public List<CalendarioHistorico> buscaRangoFecha(Date fecIni, Pageable pageable);

    @Query("select c " +
            "from CalendarioHistorico c " +
            "where c.fecha = ?1")
    public CalendarioHistorico findDate(Date fecIni);

	  @Query("SELECT c FROM CalendarioHistorico c WHERE (:fecha IS NULL OR c.fecha = :fecha) AND (:idMes IS NULL OR c.idMes = :idMes) AND (:idSemanaDiaTipo IS NULL OR c.idSemanaDiaTipo = :idSemanaDiaTipo) AND (:estado IS NULL OR c.estado = :estado) order by c.fecha asc")
	public List<CalendarioHistorico> findBySearch(
			  @Param("fecha") Date fecha,
			  @Param("idMes") Long idMes,
			  @Param("idSemanaDiaTipo") Long idSemanaDiaTipo,
			  @Param("estado") Long estado);

	  @Query("SELECT c FROM Calendario c WHERE c.idAnio = ?1")
	public List<CalendarioHistorico> findYear(Long idAnio);

}
