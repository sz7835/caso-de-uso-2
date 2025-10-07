package com.delta.deltanet.models.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.Contrato;

public interface IContratoDao extends JpaRepository<Contrato, Long> {

    @Query("SELECT c FROM Contrato c " +
            "WHERE (:idRelacion IS NULL OR c.idRelacion = :idRelacion) " +
            "AND (:idTipoContrato IS NULL OR c.idTipoContrato = :idTipoContrato) " +
            "AND (:idTipoServicio IS NULL OR c.idTipoServicio = :idTipoServicio) " +
            "AND (:idFormaPago IS NULL OR c.idFormaPago = :idFormaPago) " +
            "AND (:descripcion IS NULL OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) "
            +
            "AND (:fecIniRng1 IS NULL OR c.fecIni >= :fecIniRng1) " +
            "AND (:fecIniRng2 IS NULL OR c.fecIni <= :fecIniRng2) " +
            "AND (:fecFinRng1 IS NULL OR c.fecFin >= :fecFinRng1) " +
            "AND (:fecFinRng2 IS NULL OR c.fecFin <= :fecFinRng2) " +
            "AND (:estado IS NULL OR c.estado = :estado)")
    Page<Contrato> searchContratos(
            @Param("idRelacion") Long idRelacion,
            @Param("idTipoContrato") Long idTipoContrato,
            @Param("idTipoServicio") Long idTipoServicio,
            @Param("idFormaPago") Long idFormaPago,
            @Param("descripcion") String descripcion,
            @Param("fecIniRng1") Date fecIniRng1,
            @Param("fecIniRng2") Date fecIniRng2,
            @Param("fecFinRng1") Date fecFinRng1,
            @Param("fecFinRng2") Date fecFinRng2,
            @Param("estado") Long estado,
            Pageable pageable);

}
