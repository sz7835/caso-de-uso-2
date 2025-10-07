package com.delta.deltanet.models.dao;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmCronogramaHist;
import com.delta.deltanet.models.entity.Contrato;
import com.delta.deltanet.models.entity.Cronograma;
import com.delta.deltanet.models.entity.CronogramaModalidad;
import com.delta.deltanet.models.entity.LiquidacionCronograma;
import com.delta.deltanet.models.entity.Liquidaciones;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.TipoContrato;
import com.delta.deltanet.models.entity.TipoServicio;

@Repository
public interface LiquidacionesDao extends JpaRepository<Liquidaciones, Long> {

  @Query("SELECT l FROM Liquidaciones l WHERE " +
        "(:idCliente IS NULL OR l.idCliente = :idCliente) AND " +
        "(:fechaInicio IS NULL OR :fechaFin IS NULL OR l.fechaRegistro BETWEEN :fechaInicio AND :fechaFin) AND " +
        "(:estado IS NULL OR :estado = 0 OR l.estado = :estado)")
  List<Liquidaciones> buscarLiquidacionesConFiltros(@Param("idCliente") Integer idCliente,
                                                    @Param("fechaInicio") Date fechaInicio,
                                                    @Param("fechaFin") Date fechaFin,
                                                    @Param("estado") Integer estado
  );

  @Query("SELECT p FROM Persona p " +
        "LEFT JOIN FETCH p.perJur pj " +
        "INNER JOIN FETCH p.relaciones r " +
        "WHERE p.tipoPer.id = :tipoPersonaId " +
        "AND p.estado = 1 and r.estado = 1 and r.idTipoRel = 1" +
         // p.visible = 1")
        "AND (:razonSocial IS NULL OR LOWER(pj.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%'))) " +
        "AND (:documento IS NULL OR LOWER(p.documento) LIKE LOWER(CONCAT('%', :documento, '%')))")
  List<Persona> findPersonasJuridicasByTipoPersona(@Param("tipoPersonaId") Long tipoPersonaId,
                                                   @Param("razonSocial") String razonSocial,
                                                   @Param("documento") String documento);

  @Query("SELECT c FROM Contrato c " +
        "inner JOIN c.relacion r " +
        "inner JOIN r.persona p " +
        "inner JOIN c.contrato con " +
        "inner JOIN c.service ser " +
        "WHERE (:idTipoContrato IS NULL OR c.idTipoContrato = :idTipoContrato) " +
        "AND (:idTipoServicio IS NULL OR c.idTipoServicio = :idTipoServicio) " +
        "AND (LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) OR :descripcion IS NULL) " +
        "AND (:personaId IS NULL OR p.id = :personaId)")
  List<Contrato> findByFilters(@Param("idTipoContrato") Long idTipoContrato,
                               @Param("idTipoServicio") Long idTipoServicio,
                               @Param("descripcion") String descripcion,
                               @Param("personaId") Long personaId);

  @Query("SELECT tc FROM TipoContrato tc WHERE tc.estado = :estado")
  List<TipoContrato> findByEstadoTipoContrato(@Param("estado") Integer estado);

  @Query("SELECT ts FROM TipoServicio ts WHERE ts.estado = :estado")
  List<TipoServicio> findByEstadoTipoServicio(@Param("estado") Integer estado);

  @Query("SELECT h FROM AdmCronogramaHist h WHERE h.idContrato = :idContrato")
  List<AdmCronogramaHist> findHistoricoByIdContrato(@Param("idContrato") Long idContrato);

  @Query("SELECT m FROM CronogramaModalidad m")
  List<CronogramaModalidad> findAllModalidades();

  @Query("SELECT c FROM Cronograma c WHERE c.idContrato = :idContrato")
  List<Cronograma> findVigenteByIdContrato(@Param("idContrato") Long idContrato);

  @Query("SELECT l FROM Liquidaciones l WHERE " +
            "(:idCliente IS NULL OR l.idCliente = :idCliente) AND " +
            "(:fechaInicio IS NULL OR :fechaFin IS NULL OR l.fechaRegistro BETWEEN :fechaInicio AND :fechaFin) AND " +
            "(:estado IS NULL OR :estado = 0 OR l.estado = :estado)")
  Page<Liquidaciones> buscarLiquidacionesConFiltrosPaginado(@Param("idCliente") Integer idCliente,
                                                            @Param("fechaInicio") Date fechaInicio,
                                                            @Param("fechaFin") Date fechaFin,
                                                            @Param("estado") Integer estado,
                                                            Pageable pageable);

  @Query("from LiquidacionCronograma a where a.idLiquidacion = ?1 and a.idCronograma = ?2")
  Optional<LiquidacionCronograma> buscaLiqCro(Long idCronograma,Long idLiquidacion);


  @Query("SELECT liqc FROM LiquidacionCronograma liqc " +
	        "inner JOIN liqc.cronograma cr " +
	        "inner JOIN liqc.liquidation liq " +
	        "inner JOIN liq.persona per " +
	        "WHERE liqc.idLiquidacion = ?1")
  List<LiquidacionCronograma> ListCronos(Long id);


}