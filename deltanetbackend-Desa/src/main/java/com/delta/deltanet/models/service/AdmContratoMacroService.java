package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.AdmContratoMacro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AdmContratoMacroService {
	AdmContratoMacro crearContratoMacro(
			LocalDate fechaInicio,
			LocalDate fechaFin,
			BigDecimal monto,
			Long monedaId,
			String descripcion,
			String usuario);

	AdmContratoMacro obtenerContratoMacro(Long id);

	AdmContratoMacro actualizarContratoMacro(
			Long id,
			LocalDate fechaInicio,
			LocalDate fechaFin,
			BigDecimal monto,
			Long monedaId,
			String descripcion,
			String usuario);

	String cambiarEstadoContratoMacro(Long id, String usuario);

	List<AdmContratoMacro> buscarContratosMacro(
			LocalDate fechaInicio,
			LocalDate fechaFin,
			Long monedaId);
}