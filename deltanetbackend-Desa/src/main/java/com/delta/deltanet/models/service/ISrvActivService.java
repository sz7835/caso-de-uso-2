package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.delta.deltanet.models.entity.ServicioActividades;
import com.delta.deltanet.models.service.SrvActivServiceImpl.lstActi;

public interface ISrvActivService {
	public void delete(ServicioActividades item);
	public List<ServicioActividades> listaActividades();
	public ServicioActividades buscaById(Long id);
	public ServicioActividades save(ServicioActividades reg);
	public List<ServicioActividades> saveAll(List<ServicioActividades> reg);
	public List<String> listaPeriodos();
	
	public List<lstActi> lstFiltrado(Long idProy, Long idTipo, Long idForma, Long estado, 
			                                     Long idResolutor, Date fecIni, Date fecFin, Integer facturable, String mmFac,
			                                     String yyFac, List<Long> lstTickets);
	
	public Page<lstActi> lstFiltrado(Long idProy, Long idTipo, Long idForma, Long estado, 
                                                 Long idResolutor, Date fecIni, Date fecFin, Integer facturable, String mmFac,
                                                 String yyFac, Pageable pageable);
	
	public List<ServicioActividades> lstFiltrado(Long idCli, List<Long> lstTickets, Integer facturable);

}
