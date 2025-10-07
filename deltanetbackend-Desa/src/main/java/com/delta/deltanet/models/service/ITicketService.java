package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.delta.deltanet.models.entity.Ticket;

public interface ITicketService {
	
	public List<Ticket> findAll();
	public Ticket findById(Long id);
	public Ticket save(Ticket ticket);
	public void delete(Long id);
	public List<Ticket> findAllByAutenticacionUsuario(Long id);
	public List<Ticket> findAllNoResueltosByUsuarioCreador(Long id);
	public List<Ticket> findAllByAutenticacionUsuarioNull();
	public List<Ticket> findAllByUsuarioCreador(String usuario);
	public List<Ticket> findAllResueltos(String usuario, Long usuarioServicioId, Long areaId);
	public List<Ticket> findAllResueltosByUsuarioCreador(String usuario);
	public List<Ticket> findAllResueltosAdmin(Long areaId);
	public List<Ticket> findAllModificados(String usuario, Long usuarioServicioId);	
	public List<Ticket> findAllModificados();	
	public List<Ticket> findAllFiltro(Long idAreaOrigen,
									Long idAreaDestino,
									String tipoUsuario,
									Long idPrioridad,
									Long idCategoria,
									Long idCatalogoServicio,
									String usuarioCrea);
	public Map<String, Object> findAllFiltroPaginado(Long idAreaOrigen,
									Long idAreaDestino,
									Long usuarioServicio,
									Long idPrioridad,
									Long idCategoria,
									Long idCatalogoServicio,
									Long idTicket,
									String usuarioCrea,
									String startDate, String endDate);
	public List<Ticket> findAllporCerrar(Date fecha);
	public List<Ticket> findAllporCerrar(Date fecIni, Date fecFin);
	public List<Ticket> findAllporCerrar2(Date fecFin);

}
