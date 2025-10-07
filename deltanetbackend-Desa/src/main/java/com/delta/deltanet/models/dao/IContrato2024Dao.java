package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Contrato2024;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IContrato2024Dao extends JpaRepository<Contrato2024,Long> {
	

    @Query("select c " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "where r.idRel = ?1 and c.estado = 1")
    public Contrato2024 findByRelacion(Long id);


    @Query("select c " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join c.tipoContrato tc " +
            "where r.idRel = :id and tc.id = 2 and c.estado = 1")
    public Contrato2024 findByRelacion2(@Param("id") Long id);
	
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp ")
    public List<Object> listaContratos();
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2")
    public List<Object> listaContratos(Date fecIni, Date fecFin);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1")
    public List<Object> listaContratos(Long idRelacion);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1")
    public List<Object> listaContratos1(Long idTipoCto);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1")
    public List<Object> listaContratos2(Long idTipoSrv);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where fp.id = ?1")
    public List<Object> listaContratos3(Long idFormaPgo);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.descripcion like %?1%")
    public List<Object> listaContratos(String descrip);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.estado = ?1")
    public List<Object> listaContratos4(Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idtipoCto);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idtipoSrv);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and fp.id = ?3")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idFormaPgo);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and c.descripcion like %?3%")
    public List<Object> listaContratos(Date fecIni, Date fecFin, String descrip);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and c.estado = ?3")
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2")
    public List<Object> listaContratos1(Long idRelacion, Long idTipoSrv);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and fp.id = ?2")
    public List<Object> listaContratos2(Long idRelacion, Long idFormaPgo);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and c.descripcion like %?2%")
    public List<Object> listaContratos(Long idRelacion, String descrip);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and c.estado = ?2")
    public List<Object> listaContratos3(Long idRelacion, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2")
    public List<Object> listaContratos4(Long idTipoCto, Long idTipoSrv);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and fp.id = ?2")
    public List<Object> listaContratos5(Long idTipoCto, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and c.descripcion like %?2%")
    public List<Object> listaContratos1(Long idTipoCto, String descrip);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and c.estado = ?2")
    public List<Object> listaContratos6(Long idTipoCto, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1 and fp.id = ?2")
    public List<Object> listaContratos7(Long idTipoSrv, Long idFormaPgo);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1 and c.descripcion like %?2%")
    public List<Object> listaContratos2(Long idTipoSrv, String descrip);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1 and c.estado = ?2")
    public List<Object> listaContratos8(Long idTipoSrv, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where fp.id = ?1 and c.descripcion like %?2%")
    public List<Object> listaContratos3(Long idFormaPgo, String descrip);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where fp.id = ?1 and c.estado = ?2")
    public List<Object> listaContratos9(Long idFormaPgo, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.descripcion like %?1% and c.estado = ?2")
    public List<Object> listaContratos(String descrip, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and fp.id = ?4")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and c.estado = ?4")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4")
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and fp.id = ?4")
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idtipoCto, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and c.estado = ?4")
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3 and fp.id = ?4")
    public List<Object> listaContratos7(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idtipoSrv, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3 and c.estado = ?4")
    public List<Object> listaContratos8(Date fecIni, Date fecFin, Long idtipoSrv, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and fp.id = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and fp.id = ?3 and c.estado = ?4")
    public List<Object> listaContratos9(Date fecIni, Date fecFin, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and c.descripcion like %?3% and c.estado = ?4")
    public List<Object> listaContratos(Date fecIni, Date fecFin, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and fp.id = ?3")
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and c.descripcion like %?3%")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and c.estado = ?3")
    public List<Object> listaContratos2(Long idRelacion, Long idTipoCto, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2 and fp.id = ?3")
    public List<Object> listaContratos3(Long idRelacion, Long idTipoSrv, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2 and c.descripcion like %?3%")
    public List<Object> listaContratos3(Long idRelacion, Long idTipoSrv, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2 and c.estado =?3")
    public List<Object> listaContratos4(Long idRelacion, Long idTipoSrv, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and fp.id = ?2 and c.descripcion like %?3%")
    public List<Object> listaContratos1(Long idRelacion, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and fp.id = ?2 and c.estado = ?3")
    public List<Object> listaContratos5(Long idRelacion, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and c.descripcion like %?2% and c.estado = ?3")
    public List<Object> listaContratos(Long idRelacion, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2 and fp.id = ?3")
    public List<Object> listaContratos6(Long idTipoCto, Long idTipoSrv, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2 and c.descripcion like %?3%")
    public List<Object> listaContratos2(Long idTipoCto, Long idTipoSrv, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2 and c.estado = ?3")
    public List<Object> listaContratos7(Long idTipoCto, Long idTipoSrv, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and fp.id = ?2 and c.descripcion like %?3%")
    public List<Object> listaContratos4(Long idTipoCto, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and fp.id = ?2 and c.estado = ?3")
    public List<Object> listaContratos8(Long idTipoCto, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and c.descripcion like %?2% and c.estado = ?3")
    public List<Object> listaContratos1(Long idTipoCto, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1 and fp.id = ?2 and c.descripcion like %?3%")
    public List<Object> listaContratos5(Long idTipoSrv, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1 and fp.id = ?2 and c.estado = ?3")
    public List<Object> listaContratos9(Long idTipoSrv, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1 and c.descripcion like %?2% and c.estado = ?3")
    public List<Object> listaContratos2(Long idTipoSrv, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where fp.id = ?1 and c.descripcion like %?2% and c.estado = ?3")
    public List<Object> listaContratos3(Long idFormaPgo, String descrip, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and fp.id = ?5")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and c.descripcion like %?5%")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and c.estado = ?5")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4 and fp.id = ?5")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4 and c.descripcion like %?5%")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4 and c.estado = ?5")
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and fp.id = ?4 and c.descripcion like %?5%")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and fp.id = ?4 and c.estado = ?5")
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4 and fp.id = ?5")
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4 and c.descripcion like %?5%")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4 and c.estado = ?5")
    public List<Object> listaContratos7(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and fp.id = ?4 and c.descripcion like %?5%")
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and fp.id = ?4 and c.estado = ?5")
    public List<Object> listaContratos8(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idtipoCto, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3 and fp.id = ?4 and c.descripcion like %?5%")
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3 and fp.id = ?4 and c.estado = ?5")
    public List<Object> listaContratos9(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idtipoSrv, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and fp.id = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3 and fp.id = ?4")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3 and c.estado = ?4")
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and fp.id = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idFormaPgo,String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and fp.id = ?3 and c.estado = ?4")
    public List<Object> listaContratos2(Long idRelacion, Long idTipoCto, Long idFormaPgo,Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and c.descripcion like %?3% and c.estado = ?4")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2 and fp.id = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos2(Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2 and fp.id = ?3 and c.estado = ?4")
    public List<Object> listaContratos3(Long idRelacion, Long idTipoSrv, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2 and c.descripcion like %?3% and c.estado = ?4")
    public List<Object> listaContratos1(Long idRelacion, Long idTipoSrv, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and fp.id = ?2 and c.descripcion like %?3% and c.estado = ?4")
    public List<Object> listaContratos2(Long idRelacion, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2 and fp.id = ?3 and c.descripcion like %?4%")
    public List<Object> listaContratos3(Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2 and fp.id = ?3 and c.estado = ?4")
    public List<Object> listaContratos4(Long idTipoCto, Long idTipoSrv, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2 and c.descripcion like %?3% and c.estado = ?4")
    public List<Object> listaContratos3(Long idTipoCto, Long idTipoSrv, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and fp.id = ?2 and c.descripcion like %?3% and c.estado = ?4")
    public List<Object> listaContratos4(Long idTipoCto, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where ts.id = ?1 and fp.id = ?2 and c.descripcion like %?3% and c.estado = ?4")
    public List<Object> listaContratos5(Long idTipoSrv, Long idFormaPgo, String descrip, Long estado);

    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5 and fp.id = ?6")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5 and c.descripcion like %?6%")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5 and c.estado = ?6")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and fp.id = ?5 and c.descripcion like %?6%")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and fp.id = ?5 and c.estado = ?6")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and c.descripcion like %?5% and c.estado = ?6")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4 and fp.id = ?5 and c.descripcion like %?6%")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4 and fp.id = ?5 and c.estado = ?6")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4 and c.descripcion like %?5% and c.estado = ?6")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and fp.id = ?4 and c.descripcion like %?5% and c.estado = ?6")
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4 and fp.id = ?5 and c.descripcion like %?6%")
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4 and fp.id = ?5 and c.estado = ?6")
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4 and c.descripcion like %?5% and c.estado = ?6")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and fp.id = ?4 and c.descripcion like %?5% and c.estado = ?6")
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and ts.id = ?3 and fp.id = ?4 and c.descripcion like %?5% and c.estado = ?6")
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3 and fp.id = ?4 and c.descripcion like %?5%")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3 and fp.id = ?4 and c.estado = ?5")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip,Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and fp.id = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idFormaPgo,String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and ts.id = ?2 and fp.id = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos2(Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where tc.id = ?1 and ts.id = ?2 and fp.id = ?3 and c.descripcion like %?4% and c.estado = ?5")
    public List<Object> listaContratos3(Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5 and fp.id = ?6 and c.descripcion like %?7%")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5 and fp.id = ?6 and c.estado = ?7")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5 and c.descripcion like %?6% and c.estado = ?7")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and fp.id = ?5 and c.descripcion like %?6% and c.estado = ?7")
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo, String descrip,Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and ts.id = ?4 and fp.id = ?5 and c.descripcion like %?6% and c.estado = ?7")
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and tc.id = ?3 and ts.id = ?4 and fp.id = ?5 and c.descripcion like %?6% and c.estado = ?7")
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where r.idRel = ?1 and tc.id = ?2 and ts.id = ?3 and fp.id = ?4 and c.descripcion like %?5% and c.estado = ?6")
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo, String descrip, Long estado);
    @Query("select c.id, r.idRel, tr.descrip, tc.descrip, ts.descrip, fp.descrip, " +
            "c.descripcion, c.fecInicio, c.fecFin, c.monto, c.hes, c.estado, " +
            "c.createUser, c.createDate, c.updateUser, c.updateDate " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.tipoRel tr " +
            "inner join c.tipoContrato tc " +
            "inner join c.tipoServicio ts " +
            "inner join c.formaPago fp " +
            "where c.fecFin >= ?1 and c.fecFin <= ?2 and r.idRel = ?3 and tc.id = ?4 and ts.id = ?5 and fp.id = ?6 and c.descripcion like %?7% and c.estado = ?8")
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado);

    @Query("select pj.razonSocial " +
            "from Contrato2024 c " +
            "inner join c.relacion r " +
            "inner join r.persona p " +
            "inner join p.perJur pj " +
            "where c.id = ?1")
    public Object buscaCliente(Long id);

}
