package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Relacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IRelacionDao extends JpaRepository<Relacion, Long> {
        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni " +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1")
        List<Object> listaRelaciones(Long idPer);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1")
        List<Object> busFiltrada(Long idPer);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and tp.idTipoRel = ?2")
        List<Object> busFiltrada(Long idPer, Long idTipoRel);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and tp.idTipoRel = ?2 and (r.fecIni >= ?3 and r.fecIni <= ?4)")
        List<Object> busFiltrada(Long idPer, Long idTipoRel, Date fecIni, Date fecFin);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and tp.idTipoRel = ?2 and r.fecIni between ?3 and ?4 and r.estado = ?5")
        List<Object> busFiltrada(Long idPer, Long idTipoRel, Date fecIni, Date fecFin, Long estado);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and tp.idTipoRel = ?2 and r.estado = ?3")
        List<Object> busFiltrada(Long idPer, Long idTipoRel, Long estado);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and r.fecIni between ?2 and ?3")
        List<Object> busFiltrada(Long idPer, Date fecIni, Date fecFin);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and r.fecIni between ?2 and ?3 and r.estado = ?4")
        List<Object> busFiltrada(Long idPer, Date fecIni, Date fecFin, Long estado);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.fecFin, r.idPersona, r.idNodoDestino, r.idNodoTipo "
                        +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and r.estado = ?2")
        List<Object> busFiltrada4(Long idPer, Long estado);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.idArea " +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and r.estado = ?2")
        List<Object> busFiltrada2(Long idPer, Long estado);

        @Query("Select r.idRel, r.estado, tp.idTipoRel, tp.descrip, r.fecIni, r.idArea " +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and r.idArea = ?2 and r.estado = ?3")
        List<Object> busFiltrada3(Long idPer, Long idArea, Long estado);

        @Query("Select count(t) " +
                        "from Contrato t " +
                        "where idRelacion = ?1")
        Long nroContratos(Long idRel);

        @Query("from Relacion where idRel = ?1")
        Relacion buscaId(Long idRel);

        @Query("Select r.idRel, r.estado, r.idArea, tp.idTipoRel, tp.descrip, r.fecIni " +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "where p.id = ?1 and r.estado = 1 " +
                        "order by tp.idTipoRel asc")
        List<Object> listaRelacionesFilter(Long idPer);

        @Query("Select p.id,td.nombre, p.documento, concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) " +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "inner join p.tipoDoc td " +
                        "inner join p.perNat pn " +
                        "where (tp.id = 3 or tp.id = 4) and r.estado = 1")
        List<Object> listaPersonal();

        @Query("Select p.id,td.nombre, p.documento, concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) " +
                        "from Relacion r " +
                        "inner join r.tipoRel tp " +
                        "inner join r.persona p " +
                        "inner join p.tipoDoc td " +
                        "inner join p.perNat pn " +
                        "where tp.id = 5 and r.estado = 1")
        List<Object> listaOutsourcing();
        
        @Query("SELECT r.idRel, p, r.tipoRel FROM Relacion r " +
                "join r.persona p " +
                "where r.idTipoRel = 4 and r.estado = 1 and p.area = 6 and p.puesto = 7")
        List<Object[]> listaPersonasOutsourcing();
        
        @Query("SELECT r.idRel, p, r.fecFin FROM Persona p " +
        		"join p.perNat pn " +
        	    "LEFT JOIN p.relaciones r WITH r.idTipoRel = 4 AND r.estado = 1 " +
                "where p.visible = 1 and p.estado = 1 and p.area = 6 and p.puesto = 7 " +
                "AND (:documento IS NULL OR p.documento = :documento) " +
                "AND (:nombre IS NULL OR LOWER(pn.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) ")
        List<Object[]> listaPersonasOutsourcingNombreODocumento(String nombre, String documento);
        
        @Query("SELECT r.idRel, p, r.tipoRel FROM Relacion r " +
                "join r.persona p " +
                "join r.contratos c " +
                "where r.idTipoRel in (1,2) and r.estado = 1 and c.estado = 1 and c.idTipoContrato = 1")
        List<Object[]> listaClientesOutsourcing();

        @Query("SELECT r.idArea FROM Relacion r WHERE r.idRel = :idRel")
        Long findIdAreaByIdRel(@Param("idRel") Long idRel);

        @Query("from Relacion r where r.idArea = ?1 and r.estado = 1")
        List<Relacion> buscaPorArea(Long idArea);

        @Query("from Relacion r where r.idArea = ?1 and r.estado = 1")
        Relacion buscaReverse(Long NodoOrigen, Long NodoDestino, Long idTipo);

        @Query("SELECT r.idRel, p, r.tipoRel FROM Relacion r "
        		+ "JOIN r.persona p "
        		+ "WHERE r.idTipoRel IN (1,2) AND r.area IS NULL AND r.estado = 1 and p.visible = 1")
        List<Object[]> findRelacionesForContrato();
 
        @Query("SELECT r FROM Relacion r WHERE r.idPersona = :idPersona AND r.idTipoRel = :idTipoRel")
        Optional<Relacion> findByNodoOrigenAndMotivo(@Param("idPersona") Long idPersona, @Param("idTipoRel") Long idTipoRel);

        @Query("SELECT r FROM Relacion r WHERE r.idPersona = :idPersona")
        List<Relacion> findByNodoOrigen(@Param("idPersona") Long idPersona);
        
        @Query("SELECT r FROM Relacion r " +
        		"JOIN r.tipoRel tr " +
        		"JOIN r.persona p " +
        		"WHERE r.idNodoDestino = :idPer " +
        		"AND (:idTipoNodoDestino IS NULL OR tr.destino = :idTipoNodoDestino) " +
        		"AND (:idMotivo IS NULL OR tr.idTipoRel = :idMotivo) " +
        		"AND (:tipoDoc IS NULL OR p.tipoDoc = :tipoDoc) " +
        		"AND (:nroDoc IS NULL OR p.documento = :nroDoc) " +
        		"AND (:estado IS NULL OR r.estado = :estado) " +
        		"AND tr.tipo IN :tipoRelTipos ")
        List<Relacion> buscarRelacionContactos(@Param("idPer") Long idPer, 
        		@Param("idTipoNodoDestino") Long idTipoNodoDestino, 
        		@Param("idMotivo") Long idMotivo, 
        		@Param("tipoRelTipos") List<Integer> tipoRelTipos,
        		@Param("tipoDoc") Long tipoDoc,
        		@Param("nroDoc") String nroDoc,
        		@Param("estado") Long estado);

        @Query("SELECT r FROM Relacion r WHERE r.idPersona = :idPersona AND r.idNodoDestino = :idNodoDestino AND r.idTipoRel = :idTipoRel AND r.estado = :estado")
        Relacion findByPersonaIdAndPersonaDIdAndTipoRelIdAndEstado(@Param("idPersona") Long idPersona,
                                                                @Param("idNodoDestino") Long idNodoDestino,
                                                                @Param("idTipoRel") Long idTipoRel,
                                                                @Param("estado") Long estado);

        @Query("SELECT r FROM Relacion r WHERE r.idNodoDestino = :idPer AND r.estado = 1")
        List<Relacion> searchRelCto(@Param("idPer") Long idPersona);
}
