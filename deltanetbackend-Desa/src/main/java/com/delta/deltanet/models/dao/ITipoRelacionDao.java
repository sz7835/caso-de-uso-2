package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TipoRelacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITipoRelacionDao extends JpaRepository<TipoRelacion,Long> {
    @Query("select idTipoRel, descrip, '', estado from TipoRelacion")
    List<Object> listaRelaciones();

	@Query("from TipoRelacion tr "
			+"inner join tr.nodoOrigen no "
			+"inner join tr.nodoDestino nd ")
	List<TipoRelacion> Lista();

    @Query("from TipoRelacion where idTipoRel = ?1")
    TipoRelacion buscaId(Long idTipo);

    @Query("from TipoRelacion where tipo in (?1) and destino = ?2")
    List<TipoRelacion> findByTipo(List<Integer> tipos, Long destino);

    @Query("SELECT tr.idTipoRel FROM TipoRelacion tr WHERE tr.origen = :nodoTipoOrigenId AND tr.tipo IN :tipos")
    List<Long> findIdsByNodoTipoOrigenAndTipoIn(@Param("nodoTipoOrigenId") Long nodoTipoOrigenId, @Param("tipos") List<Integer> tipos);

    @Query("from TipoRelacion where descrip = :nombre and origen = :origen and destino = :destino and tipo = :tipo and estado <> 0")
    List<TipoRelacion> findByDescripAndOrigenAndDestinoAndTipo(@Param("nombre") String nombre, @Param("origen") Long origen, @Param("destino") Long destino, @Param("tipo") Integer tipo);

}
