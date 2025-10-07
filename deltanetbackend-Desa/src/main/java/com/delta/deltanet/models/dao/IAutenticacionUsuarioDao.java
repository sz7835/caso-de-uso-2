package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import com.delta.deltanet.models.entity.Relacion;
import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.AutenticacionUsuario;
import org.springframework.data.jpa.repository.Query;

public interface IAutenticacionUsuarioDao extends JpaRepository<AutenticacionUsuario, Long> {
	public List<AutenticacionUsuario> findAll();
	public Optional<AutenticacionUsuario> findById(Long id);
	public AutenticacionUsuario findByUsuario(String usuario);

	@Query("select u.id, u.usuario, u.tipoUsuario, u.tipoUsuarioDelta, u.codEstUsuario, " +
			"concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.id, p.documento  " +
			"from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"inner join p.perNat pn " +
			"where u.usuario = ?1")
	List<Object>  buscaUsuarioAutenticado(String usuario);
	
	@Query("select u.id, u.usuario, " +
			"concat(pn.apePaterno,' ',pn.apeMaterno, ', ', pn.nombre) " +
			"from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"inner join p.area a " +
			"inner join p.perNat pn " +
			"where u.codEstUsuario = 1 and u.tipoUsuarioDelta = 1 and a.id = 7 order by pn.apePaterno, pn.apeMaterno, pn.nombre")
	List<Object> listaRecursos();

	@Query("from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"inner join p.perNat pn " +
			"inner join p.relaciones r " +
			"inner join p.area a " +
			"where a.id = ?1 and r.estado = 1")
	List<Object> buscaUsuarioIdArea(Long idArea);

	@Query("from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"inner join p.tipoDoc td " +
			"inner join p.perNat pn " +
			"where u.tipoUsuarioDelta = ?1 and u.tipoUsuario = ?2")
	List<Object> listaUsuariosDelta(Integer typeCategory, Integer type);

	@Query("from Relacion r " +
			"inner join r.area a ")
	List<Relacion> listaRelaciones();

	@Query("from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"inner join p.relaciones r " +
			"inner join p.area a " +
			"where u.tipoUsuarioDelta = 2 order by u.id asc, r.estado asc")
	List<Object> listaUsuarios();

	@Query("from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"left join p.relaciones r " +
			"left join p.area " +
			"where u.tipoUsuarioDelta = 2 and u.id = ?1")
	AutenticacionUsuario buscaUserDelta2(Long id);

	@Query("from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"left join p.relaciones r " +
			"left join p.area " +
			"where u.tipoUsuarioDelta = 1 and u.id = ?1")
	AutenticacionUsuario buscaUserDelta(Long id);
	
	@Query("from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"inner join p.relaciones r " +
			"inner join p.area a " +
			"order by u.id asc, r.estado asc")
	List<Object> listaFullUsuarios();

	@Query("select distinct u from AutenticacionUsuario u " +
			"inner join u.persona p " +
			"left join p.relaciones r " +
			"left join p.area " +
			"where u.tipoUsuarioDelta = 1 and u.id = ?1")
	AutenticacionUsuario buscaById(Long id);
}
