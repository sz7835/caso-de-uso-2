package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ICronogramaDao extends JpaRepository<Cronograma, Long> {

        @Query("select r.idRel, p.id, pn.nombre, pn.apePaterno, pn.apeMaterno, td.nombre, p.documento " +
                        "from Relacion r " +
                        "inner join r.tipoRel tr " +
                        "inner join r.persona p " +
                        "inner join p.area a " +
                        "inner join p.puesto pu " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where r.estado = 1 and a.id = 6 and pu.id = 7 and p.visible = 1" +
                        "and tr.idTipoRel = 4")
        public List<Object> listaRelacionOutsourcing();

        @Query("select c.id, c.idContrato, c.descripcion, c.estado " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "where p.id = ?1 and c.estado = 1")
        public List<Object> listaCronoPorIdPer(Long id);

        @Query("select p.id, concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "where p.estado = 1")
        public List<Object> listaRecursos();

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), " +
                        "c.periodoInicial, c.periodoFinal, c.nroOC, c.fechaOC, c.estado " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "where c.id = ?1")
        public Object BuscaId(Long id);

        @Query("select c.id, c.descripcion, Date(c.fecIni) , Date(c.fecFin), c.vencimiento, tc.descrip, c.idTipoPersona "+
                        "from Contrato c " +
                        "inner join c.contrato tc " +
                        "where c.estado = 1")
        public List<Object> listaPorVencer();

        @Query("select COALESCE(pj.razonSocial, concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)) " +
                        "from Contrato2024 c " +
                        "inner join c.relacion r " +
                        "inner join r.persona p " +
                        "left join p.perJur pj " +
                        "left join p.perNat pn " +
                        "where c.id = ?1")
        public String ObtEmpresa(Long IdContrato);

        @Query("select au.usuario " +
                "from Contrato2024 c " +
                "inner join c.relacion r " +
                "inner join r.persona p " +
                "inner join AutenticacionUsuario au on au.persona.id = p.id " +
                "where c.id = ?1")
        public String ObtUsuario(Long IdContrato);

        @Query("select td.nombre " +
                "from Contrato2024 c " +
                "inner join c.relacion r " +
                "inner join r.persona p " +
                "inner join p.tipoDoc td " +
                "where c.id = ?1")
        public String ObtTipoDocumento(Long IdContrato);

        @Query("select p.documento " +
                "from Contrato2024 c " +
                "inner join c.relacion r " +
                "inner join r.persona p " +
                "where c.id = ?1")
        public String ObtNroDocumento(Long IdContrato);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td ")
        public List<Object> BusquedaFiltrada();

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2)")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3 " +
                        "and p.documento = ?4")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3 " +
                        "and p.documento = ?4 and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?5%")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3 " +
                        "and p.documento = ?4 and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?5% " +
                        "and c.estado = ?6")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, String nombres,
                        Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1 ")
        public List<Object> BusquedaFiltrada(Long tipoDoc);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1 and p.documento = ?2 ")
        public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1 and p.documento = ?2 " +
                        "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?3%")
        public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1 and p.documento = ?2 " +
                        "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?3% and c.estado = ?4")
        public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, String nombres, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where p.documento = ?1 ")
        public List<Object> BusquedaFiltrada(String nroDoc);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where p.documento = ?1 and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?2%")
        public List<Object> BusquedaFiltrada(String nroDoc, String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where p.documento = ?1 and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?2% " +
                        "and c.estado = ?3")
        public List<Object> BusquedaFiltrada(String nroDoc, String nombres, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?1%")
        public List<Object> BusquedaFiltrada1(String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?1% and c.estado = ?2")
        public List<Object> BusquedaFiltrada(String nombres, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where c.estado = ?1")
        public List<Object> BusquedaFiltrada1(Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and p.documento = ?3")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) " +
                        "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?3%")
        public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) " +
                        "and c.estado = ?3")
        public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1 and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?2%")
        public List<Object> BusquedaFiltrada1(Long tipoDoc, String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1 and c.estado = ?2")
        public List<Object> BusquedaFiltrada(Long tipoDoc, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where p.documento = ?1 and c.estado = ?2")
        public List<Object> BusquedaFiltrada1(String nroDoc, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3 " +
                        "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4%")
        public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long tipoDoc, String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3 " +
                        "and c.estado = ?4")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) " +
                        "and p.documento = ?3 and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4%")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, String nombres);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) " +
                        "and p.documento = ?3 and c.estado = ?4")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?3% " +
                        "and c.estado = ?4")
        public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, String nombres, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1 and p.documento = ?2 " +
                        "and c.estado = ?3")
        public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where td.idTipDoc = ?1  " +
                        "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?2% and c.estado = ?3")
        public List<Object> BusquedaFiltrada1(Long tipoDoc, String nombres, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) " +
                        "and p.documento = ?3 and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4% " +
                        "and c.estado = ?5")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, String nombres, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3 " +
                        "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4% " +
                        "and c.estado = ?5")
        public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nombres, Long estado);

        @Query("select c.id, c.idContrato, c.descripcion, " +
                        "c.periodoInicial, c.periodoFinal, " +
                        "c.nroOC, c.fechaOC, c.estado, p.id, " +
                        "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), td.nombre, p.documento " +
                        "from Cronograma c " +
                        "inner join c.persona p " +
                        "inner join p.perNat pn " +
                        "inner join p.tipoDoc td " +
                        "where (c.periodoFinal >= ?1 and c.periodoFinal <= ?2) and td.idTipDoc = ?3 " +
                        "and p.documento = ?4 and c.estado = ?5")
        public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, Long estado);

    @Query("select c from Cronograma c " +
            "inner join c.modalidad mo " +
            "inner join c.persona p " +
            "inner join p.perNat pn " +
            "inner join p.tipoDoc td " +
            "left join c.liquids li " +
            "where (c.idContrato = ?1) and li.id is null")
    public List<Cronograma> BusquedaCronogramas(Long contactID);

        @Query("SELECT c FROM Cronograma c WHERE c.persona.id = :idPersona AND c.estado = :estado")
        List<Cronograma> findByPersonaIdAndEstado(@Param("idPersona") Long idPersona, @Param("estado") Long estado);

        @Query("SELECT c FROM Cronograma c WHERE c.estado = :estado")
        List<Cronograma> findByEstado(@Param("estado") Long estado);
}
