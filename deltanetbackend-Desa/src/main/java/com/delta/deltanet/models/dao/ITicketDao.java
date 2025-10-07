package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.Ticket;

public interface ITicketDao extends JpaRepository<Ticket, Long> {

	@Query("from Ticket where autenticacionUsuario.id = ?1 order by id desc")
	public List<Ticket> findAllByAutenticacionUsuario(Long idUsuarioServicio);
	
	@Query("from Ticket where autenticacionUsuario.id = null")
	public List<Ticket> findAllByAutenticacionUsuarioNull();
	
	@Query("from Ticket where usuarioCreador = ?1 order by id desc")
	public List<Ticket> findAllByUsuarioCreador(String usuario);
	
	@Query("from Ticket where estado.id in (4,5) and (usuarioCreador = ?1 or autenticacionUsuario.id = ?2 or areaDestino.id = ?3) order by id desc")
	public List<Ticket> findAllResueltos(String usuario, Long usuarioServicioId, Long areaId);

	@Query("from Ticket where estado.id in (1,2) and autenticacionUsuario.id = ?1")
	public List<Ticket> findAllNoResueltosByUsuarioCreador(Long idUsuarioServicio);
	
	@Query("from Ticket where estado.id in (4,5) and usuarioCreador = ?1")
	public List<Ticket> findAllResueltosByUsuarioCreador(String usuario);
	
	@Query("from Ticket where estado.id in (4,5) and areaDestino.id = ?1")
	public List<Ticket> findAllResueltosAdmin(Long areaId);
	
	@Query("from Ticket where fechaEditado <> null and fechaEditado <= CURRENT_TIMESTAMP and (usuarioCreador = ?1 or autenticacionUsuario.id = ?2) order by fechaEditado desc")
	public List<Ticket> findAllModificados(String usuario, Long usuarioServicioId);

	@Query("from Ticket where fechaEditado <> null and fechaEditado <= CURRENT_TIMESTAMP order by fechaEditado desc")
	public List<Ticket> findAllModificados();

	@Query("from Ticket where fechaEditado is not null and DATE(fechaEditado) = ?1 and estado_id = 4 order by usuario_servicio_id asc")
	public List<Ticket> findAllporCerrar(Date fecha);

	@Query("from Ticket where fechaEditado is not null and ?1 >= DATE(fechaEditado) and estado_id = 4 order by usuario_servicio_id asc")
	public List<Ticket> findAllporCerrar(Date fecIni, Date fecFin);
	
	@Query("from Ticket where fechaEditado is not null and DATE(fechaEditado) <= ?1 and estado_id = 4 order by usuario_servicio_id asc")
	public List<Ticket> findAllporCerrar2(Date fecFin);

}
