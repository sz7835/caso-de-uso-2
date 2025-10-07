package com.delta.deltanet.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.ServicioInforme;

public interface ISrvInformeDao extends JpaRepository<ServicioInforme, Long> {
	
	@Query("SELECT MAX(si.numero) FROM ServicioInforme si WHERE si.cliente.idPerJur = :idCliente")
	Optional<Integer> findMaxNumeroByCliente(@Param("idCliente") Long idCliente);
	
	boolean existsByCliente_IdPerJurAndMesFac(Long idCliente, String mesFac);
}
