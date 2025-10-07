package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.NodoTipo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface INodoTipoDao extends JpaRepository<NodoTipo,Long> {
	@Query("SELECT COUNT(n) FROM NodoTipo n WHERE LOWER(TRIM(n.abrev)) = LOWER(TRIM(:acronimo)) AND n.estado = 1 AND (:exceptId IS NULL OR n.id <> :exceptId)")
	long countByAcronimoActivo(@org.springframework.data.repository.query.Param("acronimo") String acronimo, @org.springframework.data.repository.query.Param("exceptId") Long exceptId);

	@Query("SELECT COUNT(n) FROM NodoTipo n WHERE LOWER(TRIM(n.nombre)) = LOWER(TRIM(:nombre)) AND n.estado = 1 AND (:exceptId IS NULL OR n.id <> :exceptId)")
	long countByNombreActivo(@org.springframework.data.repository.query.Param("nombre") String nombre, @org.springframework.data.repository.query.Param("exceptId") Long exceptId);

	@Query("select f from NodoTipo f where f.abrev = ?1")
	public NodoTipo searchNodo(String type);

}
