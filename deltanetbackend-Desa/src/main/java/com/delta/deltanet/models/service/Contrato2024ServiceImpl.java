package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IContrato2024Dao;
import com.delta.deltanet.models.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Contrato2024ServiceImpl implements IContrato2024Service {
    @Autowired
    public IContrato2024Dao contratoDao;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Contrato2024 Save(Contrato2024 contrato) {
        return contratoDao.save(contrato);
    }

    @Override
    public Contrato2024 findById(Long id) {
        return contratoDao.findById(id).orElse(null);
    }

    @Override
    public Contrato2024 findByRelacion(Long id) {
        return contratoDao.findByRelacion(id);
    }

    @Override
    public Contrato2024 findByRelacion2(Long id) {
        return contratoDao.findByRelacion2(id);
    }

    @Override
    public List<Object> listadoFiltrado(Long idRelacion, Long idTipoContrato, Long idTipoServicio, Long idFormaPago,
                                        String descripcion, Date fecIniRng1, Date fecIniRng2,
                                        Date fecFinRng1, Date fecFinRng2, Long estado) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contrato2024> cq = cb.createQuery(Contrato2024.class);//.distinct(true); // Asegurar resultados únicos
        Root<Contrato2024> contratoRoot = cq.from(Contrato2024.class);
        List<Predicate> predicates = new ArrayList<>();

        Join<Contrato2024, Relacion> relacionJoin = contratoRoot.join("relacion", JoinType.INNER);
        Join<Relacion, TipoRelacion> tipoRelacionJoin = relacionJoin.join("tipoRel", JoinType.INNER);
        Join<Contrato2024, TipoContrato> tipoContratoJoin = contratoRoot.join("tipoContrato", JoinType.INNER);
        Join<Contrato2024, TipoServicio> tipoServicioJoin = contratoRoot.join("tipoServicio", JoinType.INNER);
        Join<Contrato2024, FormaPago> formaPagoJoin = contratoRoot.join("formaPago", JoinType.INNER);

        

        if(idRelacion!=null && idRelacion!=0) predicates.add(cb.equal(relacionJoin.get("idRel"),idRelacion));
        if(idTipoContrato!=null && idTipoContrato!=0) predicates.add(cb.equal(tipoContratoJoin.get("id"),idTipoContrato));
        if(idTipoServicio!=null && idTipoServicio!=0) predicates.add(cb.equal(tipoServicioJoin.get("id"),idTipoServicio));
        if(idFormaPago!=null && idFormaPago!=0) predicates.add(cb.equal(formaPagoJoin.get("id"),idFormaPago));

        if(descripcion!=null) predicates.add(cb.like(contratoRoot.get("descripcion"),"%" + descripcion + "%"));
        if(fecIniRng1!=null && fecIniRng2!=null) predicates.add(cb.between(contratoRoot.get("fecInicio"), fecIniRng1, fecIniRng2));
        if(fecFinRng1!=null && fecFinRng2!=null) predicates.add(cb.between(contratoRoot.get("fecFin"), fecFinRng1, fecFinRng2));
        if(estado!=null) predicates.add(cb.equal(contratoRoot.get("estado"),estado));

        cq.where(predicates.toArray(new Predicate[0]));

        List<Contrato2024> contratos = entityManager.createQuery(cq).getResultList();


        return contratos.stream().map(contrato -> {
            lstContrato lista = new lstContrato();
            lista.setIdContrato(contrato.getId());
            lista.setIdRelacion(contrato.getRelacion().getIdRel());
            lista.setDesTipoRel(contrato.getRelacion().getTipoRel().getDescrip());
            lista.setDesTipoCto(contrato.getTipoContrato().getDescrip());
            lista.setDesTipoSrv(contrato.getTipoServicio().getDescrip());
            lista.setDesFormPgo(contrato.getFormaPago().getDescrip());
            lista.setDescripcion(contrato.getDescripcion());
            lista.setFecIni(contrato.getFecInicio());
            lista.setFecFin(contrato.getFecFin());
            lista.setMonto(contrato.getMonto());
            lista.setHes(contrato.getHes());
            lista.setEstado(contrato.getEstado());
            lista.setCreateUser(contrato.getCreateUser());
            lista.setCreateDate(contrato.getCreateDate());
            lista.setUpdateUser(contrato.getUpdateUser());
            lista.setUpdateDate(contrato.getUpdateDate());
            return lista;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<Object> listadoFiltrado(Long idRelacion, Long idTipoContrato, Long idTipoServicio, Long idFormaPago,
                                        String descripcion, Date fecIniRng1, Date fecIniRng2,
                                        Date fecFinRng1, Date fecFinRng2, Long estado,
                                        Integer column, String dir, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contrato2024> cq = cb.createQuery(Contrato2024.class);//.distinct(true); // Asegurar resultados únicos
        Root<Contrato2024> contratoRoot = cq.from(Contrato2024.class);
        List<Predicate> predicates = new ArrayList<>();

        Join<Contrato2024, Relacion> relacionJoin = contratoRoot.join("relacion", JoinType.INNER);
        Join<Relacion, TipoRelacion> tipoRelacionJoin = relacionJoin.join("tipoRel", JoinType.INNER);
        Join<Contrato2024, TipoContrato> tipoContratoJoin = contratoRoot.join("tipoContrato", JoinType.INNER);
        Join<Contrato2024, TipoServicio> tipoServicioJoin = contratoRoot.join("tipoServicio", JoinType.INNER);
        Join<Contrato2024, FormaPago> formaPagoJoin = contratoRoot.join("formaPago", JoinType.INNER);

        if(idRelacion!=null && idRelacion!=0) predicates.add(cb.equal(relacionJoin.get("idRel"),idRelacion));
        if(idTipoContrato!=null && idTipoContrato!=0) predicates.add(cb.equal(tipoContratoJoin.get("id"),idTipoContrato));
        if(idTipoServicio!=null && idTipoServicio!=0) predicates.add(cb.equal(tipoServicioJoin.get("id"),idTipoServicio));
        if(idFormaPago!=null && idFormaPago!=0) predicates.add(cb.equal(formaPagoJoin.get("id"),idFormaPago));

        if(descripcion!=null) predicates.add(cb.like(contratoRoot.get("descripcion"),"%" + descripcion + "%"));
        if(fecIniRng1!=null && fecIniRng2!=null) predicates.add(cb.between(contratoRoot.get("fecInicio"), fecIniRng1, fecIniRng2));
        if(fecFinRng1!=null && fecFinRng2!=null) predicates.add(cb.between(contratoRoot.get("fecFin"), fecFinRng1, fecFinRng2));
        if(estado!=null) predicates.add(cb.equal(contratoRoot.get("estado"),estado));

        cq.where(predicates.toArray(new Predicate[0]));

        List<Contrato2024> contratos = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());


        List<Object> dataContratos = contratos.stream().map(contrato -> {
            lstContrato lista = new lstContrato();
            lista.setIdContrato(contrato.getId());
            lista.setIdRelacion(contrato.getRelacion().getIdRel());
            lista.setDesTipoRel(contrato.getRelacion().getTipoRel().getDescrip());
            lista.setDesTipoCto(contrato.getTipoContrato().getDescrip());
            lista.setDesTipoSrv(contrato.getTipoServicio().getDescrip());
            lista.setDesFormPgo(contrato.getFormaPago().getDescrip());
            lista.setDescripcion(contrato.getDescripcion());
            lista.setFecIni(contrato.getFecInicio());
            lista.setFecFin(contrato.getFecFin());
            lista.setMonto(contrato.getMonto());
            lista.setHes(contrato.getHes());
            lista.setEstado(contrato.getEstado());
            lista.setCreateUser(contrato.getCreateUser());
            lista.setCreateDate(contrato.getCreateDate());
            lista.setUpdateUser(contrato.getUpdateUser());
            lista.setUpdateDate(contrato.getUpdateDate());
            return lista;
        }).collect(Collectors.toList());

        Page<Object> result1 = new PageImpl<>(dataContratos,pageable,total);
        return result1;
    }

    @Override
    public List<Object> listaContratos() {
        return contratoDao.listaContratos();
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin) {
        return contratoDao.listaContratos(fecIni, fecFin);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion) {
        return contratoDao.listaContratos(idRelacion);
    }

    @Override
    public List<Object> listaContratos1(Long idTipoCto) {
        return contratoDao.listaContratos1(idTipoCto);
    }

    @Override
    public List<Object> listaContratos2(Long idTipoSrv) {
        return contratoDao.listaContratos2(idTipoSrv);
    }

    @Override
    public List<Object> listaContratos3(Long idFormaPgo) {
        return contratoDao.listaContratos3(idFormaPgo);
    }

    @Override
    public List<Object> listaContratos(String descrip) {
        return contratoDao.listaContratos(descrip);
    }

    @Override
    public List<Object> listaContratos4(Long estado) {
        return contratoDao.listaContratos4(estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion) {
        return contratoDao.listaContratos(fecIni, fecFin,idRelacion);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idtipoCto) {
        return contratoDao.listaContratos1(fecIni, fecFin, idtipoCto);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idtipoSrv) {
        return contratoDao.listaContratos2(fecIni, fecFin, idtipoSrv);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idFormaPgo) {
        return contratoDao.listaContratos3(fecIni, fecFin, idFormaPgo);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, String descrip) {
        return contratoDao.listaContratos(fecIni, fecFin, descrip);
    }

    @Override
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long estado) {
        return contratoDao.listaContratos4(fecIni, fecFin, estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto) {
        return contratoDao.listaContratos(idRelacion,idTipoCto);
    }

    @Override
    public List<Object> listaContratos1(Long idRelacion, Long idTipoSrv) {
        return contratoDao.listaContratos1(idRelacion, idTipoSrv);
    }

    @Override
    public List<Object> listaContratos2(Long idRelacion, Long idFormaPgo) {
        return contratoDao.listaContratos2(idRelacion, idFormaPgo);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, String descrip) {
        return contratoDao.listaContratos(idRelacion,descrip);
    }

    @Override
    public List<Object> listaContratos3(Long idRelacion, Long estado) {
        return contratoDao.listaContratos3(idRelacion, estado);
    }

    @Override
    public List<Object> listaContratos4(Long idTipoCto, Long idTipoSrv) {
        return contratoDao.listaContratos4(idTipoCto, idTipoSrv);
    }

    @Override
    public List<Object> listaContratos5(Long idTipoCto, Long idFormaPgo) {
        return contratoDao.listaContratos5(idTipoCto, idFormaPgo);
    }

    @Override
    public List<Object> listaContratos1(Long idTipoCto, String descrip) {
        return contratoDao.listaContratos1(idTipoCto, descrip);
    }

    @Override
    public List<Object> listaContratos6(Long idTipoCto, Long estado) {
        return contratoDao.listaContratos6(idTipoCto, estado);
    }

    @Override
    public List<Object> listaContratos7(Long idTipoSrv, Long idFormaPgo) {
        return contratoDao.listaContratos7(idTipoSrv, idFormaPgo);
    }

    @Override
    public List<Object> listaContratos2(Long idTipoSrv, String descrip) {
        return contratoDao.listaContratos2(idTipoSrv, descrip);
    }

    @Override
    public List<Object> listaContratos8(Long idTipoSrv, Long estado) {
        return contratoDao.listaContratos8(idTipoSrv, estado);
    }

    @Override
    public List<Object> listaContratos3(Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos3(idFormaPgo, descrip);
    }

    @Override
    public List<Object> listaContratos9(Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos9(idFormaPgo, estado);
    }

    @Override
    public List<Object> listaContratos(String descrip, Long estado) {
        return contratoDao.listaContratos(descrip, estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv) {
        return contratoDao.listaContratos1(fecIni,fecFin,idRelacion,idTipoSrv);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo) {
        return contratoDao.listaContratos2(fecIni,fecFin,idRelacion,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, String descrip) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,descrip);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long estado) {
        return contratoDao.listaContratos3(fecIni,fecFin,idRelacion,estado);
    }

    @Override
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv) {
        return contratoDao.listaContratos4(fecIni,fecFin,idtipoCto,idTipoSrv);
    }

    @Override
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo) {
        return contratoDao.listaContratos5(fecIni, fecFin, idtipoCto, idFormaPgo);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idtipoCto, String descrip) {
        return contratoDao.listaContratos1(fecIni,fecFin,idtipoCto,descrip);
    }

    @Override
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long estado) {
        return contratoDao.listaContratos6(fecIni,fecFin,idtipoCto,estado);
    }

    @Override
    public List<Object> listaContratos7(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo) {
        return contratoDao.listaContratos7(fecIni,fecFin,idtipoSrv,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idtipoSrv, String descrip) {
        return contratoDao.listaContratos2(fecIni,fecFin,idtipoSrv,descrip);
    }

    @Override
    public List<Object> listaContratos8(Date fecIni, Date fecFin, Long idtipoSrv, Long estado) {
        return contratoDao.listaContratos8(fecIni,fecFin,idtipoSrv,estado);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos3(fecIni,fecFin,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos9(Date fecIni, Date fecFin, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos9(fecIni,fecFin,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, String descrip, Long estado) {
        return contratoDao.listaContratos(fecIni,fecFin,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,idTipoSrv);
    }

    @Override
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idFormaPgo) {
        return contratoDao.listaContratos1(idRelacion,idTipoCto,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, String descrip) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,descrip);
    }

    @Override
    public List<Object> listaContratos2(Long idRelacion, Long idTipoCto, Long estado) {
        return contratoDao.listaContratos2(idRelacion,idTipoCto,estado);
    }

    @Override
    public List<Object> listaContratos3(Long idRelacion, Long idTipoSrv, Long idFormaPgo) {
        return contratoDao.listaContratos3(idRelacion,idTipoSrv,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos3(Long idRelacion, Long idTipoSrv, String descrip) {
        return contratoDao.listaContratos3(idRelacion,idTipoSrv,descrip);
    }

    @Override
    public List<Object> listaContratos4(Long idRelacion, Long idTipoSrv, Long estado) {
        return contratoDao.listaContratos4(idRelacion,idTipoSrv,estado);
    }

    @Override
    public List<Object> listaContratos1(Long idRelacion, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos1(idRelacion,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos5(Long idRelacion, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos5(idRelacion,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, String descrip, Long estado) {
        return contratoDao.listaContratos(idRelacion,descrip,estado);
    }

    @Override
    public List<Object> listaContratos6(Long idTipoCto, Long idTipoSrv, Long idFormaPgo) {
        return contratoDao.listaContratos6(idTipoCto,idTipoSrv,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos2(Long idTipoCto, Long idTipoSrv, String descrip) {
        return contratoDao.listaContratos2(idTipoCto,idTipoSrv,descrip);
    }

    @Override
    public List<Object> listaContratos7(Long idTipoCto, Long idTipoSrv, Long estado) {
        return contratoDao.listaContratos7(idTipoCto,idTipoSrv,estado);
    }

    @Override
    public List<Object> listaContratos4(Long idTipoCto, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos4(idTipoCto,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos8(Long idTipoCto, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos8(idTipoCto,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos1(Long idTipoCto, String descrip, Long estado) {
        return contratoDao.listaContratos1(idTipoCto,descrip,estado);
    }

    @Override
    public List<Object> listaContratos5(Long idTipoSrv, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos5(idTipoSrv,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos9(Long idTipoSrv, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos9(idTipoSrv, idFormaPgo, estado);
    }

    @Override
    public List<Object> listaContratos2(Long idTipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos2(idTipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos3(Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos3(idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo) {
        return contratoDao.listaContratos1(fecIni,fecFin,idRelacion,idTipoCto,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, String descrip) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,descrip);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long estado) {
        return contratoDao.listaContratos2(fecIni,fecFin,idRelacion,idTipoCto,estado);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo) {
        return contratoDao.listaContratos3(fecIni,fecFin,idRelacion,idTipoSrv,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, String descrip) {
        return contratoDao.listaContratos1(fecIni,fecFin,idRelacion,idTipoSrv,descrip);
    }

    @Override
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long estado) {
        return contratoDao.listaContratos4(fecIni,fecFin,idRelacion,idTipoSrv,estado);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos2(fecIni,fecFin,idRelacion,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos5(fecIni,fecFin,idRelacion,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, String descrip, Long estado) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,descrip,estado);
    }

    @Override
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo) {
        return contratoDao.listaContratos6(fecIni,fecFin,idtipoCto,idTipoSrv,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, String descrip) {
        return contratoDao.listaContratos3(fecIni,fecFin,idtipoCto,idTipoSrv,descrip);
    }

    @Override
    public List<Object> listaContratos7(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long estado) {
        return contratoDao.listaContratos7(fecIni,fecFin,idtipoCto,idTipoSrv,estado);
    }

    @Override
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos4(fecIni,fecFin,idtipoCto,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos8(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos8(fecIni,fecFin,idtipoCto,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idtipoCto, String descrip, Long estado) {
        return contratoDao.listaContratos1(fecIni,fecFin,idtipoCto,descrip,estado);
    }

    @Override
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos5(fecIni,fecFin,idtipoSrv,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos9(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos9(fecIni,fecFin,idtipoSrv,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idtipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos2(fecIni,fecFin,idtipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos3(fecIni,fecFin,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,idTipoSrv,idformaPgo);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,idTipoSrv,descrip);
    }

    @Override
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long estado) {
        return contratoDao.listaContratos1(idRelacion,idTipoCto,idTipoSrv,estado);
    }

    @Override
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos1(idRelacion,idTipoCto,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos2(Long idRelacion, Long idTipoCto, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos2(idRelacion,idTipoCto,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, String descrip, Long estado) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,descrip,estado);
    }

    @Override
    public List<Object> listaContratos2(Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos2(idRelacion,idTipoSrv,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos3(Long idRelacion, Long idTipoSrv, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos3(idRelacion,idTipoSrv,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos1(Long idRelacion, Long idTipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos1(idRelacion,idTipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos2(Long idRelacion, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos2(idRelacion,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos3(Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos3(idTipoCto,idTipoSrv,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos4(Long idTipoCto, Long idTipoSrv, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos4(idTipoCto,idTipoSrv,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos3(Long idTipoCto, Long idTipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos3(idTipoCto,idTipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos4(Long idTipoCto, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos4(idTipoCto,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos5(Long idTipoSrv, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos5(idTipoSrv,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv,idFormaPgo);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv,descrip);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long estado) {
        return contratoDao.listaContratos1(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv,estado);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos1(fecIni,fecFin,idRelacion,idTipoCto,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos2(fecIni,fecFin,idRelacion,idTipoCto,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, String descrip, Long estado) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,descrip,estado);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos3(fecIni,fecFin,idRelacion,idTipoSrv,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos3(fecIni,fecFin,idRelacion,idTipoSrv,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos1(fecIni,fecFin,idRelacion,idTipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos2(Date fecIni, Date fecFin, Long idRelacion, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos2(fecIni,fecFin,idRelacion,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos6(fecIni,fecFin,idtipoCto,idTipoSrv,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos6(fecIni,fecFin,idtipoCto,idTipoSrv,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos3(fecIni,fecFin,idtipoCto,idTipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos4(Date fecIni, Date fecFin, Long idtipoCto, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos4(fecIni,fecFin,idtipoCto,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos5(Date fecIni, Date fecFin, Long idtipoSrv, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos5(fecIni,fecFin,idtipoSrv,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo, String descrip) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,idTipoSrv,idformaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo, Long estado) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,idTipoSrv,idformaPgo,estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,idTipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos1(Long idRelacion, Long idTipoCto, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos1(idRelacion,idTipoCto,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos2(Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos2(idRelacion,idTipoSrv,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos3(Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos3(idTipoCto,idTipoSrv,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv,idFormaPgo,descrip);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo, Long estado) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv,idFormaPgo,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, String descrip, Long estado) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv,descrip,estado);
    }

    @Override
    public List<Object> listaContratos1(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos1(fecIni,fecFin,idRelacion,idTipoCto,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos3(Date fecIni, Date fecFin, Long idRelacion, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos3(fecIni,fecFin,idRelacion,idTipoSrv,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos6(Date fecIni, Date fecFin, Long idtipoCto, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos6(fecIni,fecFin,idtipoCto,idTipoSrv,idFormaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idformaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos(idRelacion,idTipoCto,idTipoSrv,idformaPgo,descrip,estado);
    }

    @Override
    public List<Object> listaContratos(Date fecIni, Date fecFin, Long idRelacion, Long idTipoCto, Long idTipoSrv, Long idFormaPgo, String descrip, Long estado) {
        return contratoDao.listaContratos(fecIni,fecFin,idRelacion,idTipoCto,idTipoSrv,idFormaPgo,descrip,estado);
    }

    @Override
    public Object buscaCliente(Long id) {
        return contratoDao.buscaCliente(id);
    }

}
