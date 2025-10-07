package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IContratoContactoDao;
import com.delta.deltanet.models.entity.Contrato;
import com.delta.deltanet.models.entity.ContratoContacto;
import com.delta.deltanet.models.entity.lstContract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class ContratoContactoServiceImpl implements IContratoContactoService{

    @Autowired
    private IContratoContactoDao contratoContactoDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ContratoContacto save(ContratoContacto contratoContacto) {
        return contratoContactoDao.save(contratoContacto);
    }

    @Override
    public Optional<ContratoContacto> busca(Long idContrato, Long idContacto) {
        return contratoContactoDao.busca(idContrato,idContacto);
    }

    @Override
    public void delete(Long id) {
        contratoContactoDao.deleteById(id);
    }

    @Override
    public List<ContratoContacto> getContactos(Long idContrato) {
        return contratoContactoDao.getContactos(idContrato);
    }

    @Override
    public List<lstContract> listadoFiltrado(String descrip, Date fecIni, Date fecFin, Long estado, Integer alertaVencimiento) {
        Date fechaActual = new Date();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contrato> cq = cb.createQuery(Contrato.class);
        Root<Contrato> cronoRoot = cq.from(Contrato.class);
        List<Predicate> predicates = new ArrayList<>();

        // Filtrar por descripción
        if (descrip != null && !descrip.trim().isEmpty())
            predicates.add(cb.like(cb.lower(cronoRoot.get("descripcion")), "%" + descrip.toLowerCase() + "%"));
        // Filtrar por fechas de inicio y fin
        if (fecIni != null && fecFin != null)
            predicates.add(cb.between(cronoRoot.get("fecFin"), fecIni, fecFin));
        // Filtrar por estado
        if (estado != null)
            predicates.add(cb.equal(cronoRoot.get("estado"), estado));

        cq.where(predicates.toArray(new Predicate[0]));
        List<Contrato> cronogramas = entityManager.createQuery(cq).getResultList();

        List<lstContract> lstResumen = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);

        for (Contrato reg : cronogramas) {
            lstContract lista = new lstContract();

            // Cliente
            if (reg.getRelacion() != null && reg.getRelacion().getPersona() != null) {
                if (reg.getRelacion().getPersona().getPerJur() == null) {
                    lista.setClient(
                        reg.getRelacion().getPersona().getPerNat().getNombre() + " " +
                        reg.getRelacion().getPersona().getPerNat().getApePaterno() + " " +
                        reg.getRelacion().getPersona().getPerNat().getApeMaterno()
                    );
                } else {
                    lista.setClient(reg.getRelacion().getPersona().getPerJur().getRazonSocial());
                }
            }

            lista.setIdContrato(reg.getId());
            lista.setDescripcion(reg.getDescripcion());
            if (reg.getContrato() != null)
                lista.setTipo(reg.getContrato().getDescrip());
            if (reg.getFecIni() != null)
                lista.setPeriodoInicial(formatter.format(reg.getFecIni()));
            if (reg.getFecFin() != null)
                lista.setPeriodoFinal(formatter.format(reg.getFecFin()));

            lista.setEstado(reg.getEstado());
            if (reg.getEstado() == 1)
                lista.setDescEstado("Activo");
            if (reg.getEstado() == 2)
                lista.setDescEstado("Desactivado");
            if (reg.getEstado() == 3)
                lista.setDescEstado("Creado");
            if (reg.getEstado() == 4)
                lista.setDescEstado("Terminado");

            // Limpiar la fecha de vencimiento (fecFin)
            Calendar calVencimiento = Calendar.getInstance();
            calVencimiento.setTime(reg.getFecFin());
            calVencimiento.set(Calendar.HOUR_OF_DAY, 0);
            calVencimiento.set(Calendar.MINUTE, 0);
            calVencimiento.set(Calendar.SECOND, 0);
            calVencimiento.set(Calendar.MILLISECOND, 0);

            Date fecFinSinHora = calVencimiento.getTime();

            // Limpiar también la fecha actual
            Calendar calActual = Calendar.getInstance();
            calActual.setTime(fechaActual);
            calActual.set(Calendar.HOUR_OF_DAY, 0);
            calActual.set(Calendar.MINUTE, 0);
            calActual.set(Calendar.SECOND, 0);
            calActual.set(Calendar.MILLISECOND, 0);

            fechaActual = calActual.getTime();

            // Calcular la diferencia en días
            long diffInMillies = fechaActual.getTime() - fecFinSinHora.getTime();
            long diasDiferencia = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            lista.setPorVencer(String.valueOf(diasDiferencia));

            // Si alertaVencimiento es null, tomar el valor de vencimiento del contrato o 10
            int alerta = (alertaVencimiento != null ? alertaVencimiento : (reg.getVencimiento() != null ? reg.getVencimiento() : 10))* -1;
            boolean vencido = diasDiferencia >= alerta;
            lista.setVencido(vencido);

            lstResumen.add(lista);
        }
        return lstResumen;
    }

    @Override
    public Page<lstContract> listadoFiltrado(String descrip, Date fecIni, Date fecFin, Long estado, Integer alertaVencimiento, Integer column, String dir, Pageable pageable) {
        Date fechaActual = new Date();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contrato> cq = cb.createQuery(Contrato.class);
        Root<Contrato> cronoRoot = cq.from(Contrato.class);
        List<Predicate> predicates = new ArrayList<>();

        if (descrip != null && !descrip.trim().isEmpty())
            predicates.add(cb.like(cb.lower(cronoRoot.get("descripcion")), "%" + descrip.toLowerCase() + "%"));
        if (fecIni != null && fecFin != null)
            predicates.add(cb.between(cronoRoot.get("fecFin"), fecIni, fecFin));
        if (estado != null)
            predicates.add(cb.equal(cronoRoot.get("estado"), estado));

        //predicates.add(cb.equal(cronoRoot.get("idTipoContrato"), 1L));
        cq.where(predicates.toArray(new Predicate[0]));
        List<Contrato> cronogramas = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();
        Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());

        List<lstContract> lstResumen = new ArrayList<>();
        lstContract lista;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        for (Contrato reg : cronogramas) {
            lista = new lstContract();
            if(reg.getRelacion().getPersona().getPerJur() == null) {
                lista.setClient(reg.getRelacion().getPersona().getPerNat().getNombre()+" "+reg.getRelacion().getPersona().getPerNat().getApePaterno()+ " "+reg.getRelacion().getPersona().getPerNat().getApeMaterno());
            }else {
                lista.setClient(reg.getRelacion().getPersona().getPerJur().getRazonSocial());
            }
            lista.setIdContrato((long)reg.getId());
            lista.setDescripcion(reg.getDescripcion());
            lista.setTipo(reg.getContrato().getDescrip());
            if (reg.getFecIni() != null)
                lista.setPeriodoInicial(formatter.format(reg.getFecIni()));
            if (reg.getFecFin() != null)
                lista.setPeriodoFinal(formatter.format(reg.getFecFin()));

            lista.setEstado(reg.getEstado());
            if (reg.getEstado() == 1)
                lista.setDescEstado("Activo");
            if (reg.getEstado() == 2)
                lista.setDescEstado("Desactivado");
            if (reg.getEstado() == 3)
                lista.setDescEstado("Creado");
            if (reg.getEstado() == 4)
                lista.setDescEstado("Terminado");

           // Limpiar la fecha de vencimiento (fecFin)
            Calendar calVencimiento = Calendar.getInstance();
            calVencimiento.setTime(reg.getFecFin());
            calVencimiento.set(Calendar.HOUR_OF_DAY, 0);
            calVencimiento.set(Calendar.MINUTE, 0);
            calVencimiento.set(Calendar.SECOND, 0);
            calVencimiento.set(Calendar.MILLISECOND, 0);

            Date fecFinSinHora = calVencimiento.getTime();

            // Limpiar también la fecha actual
            Calendar calActual = Calendar.getInstance();
            calActual.setTime(fechaActual);
            calActual.set(Calendar.HOUR_OF_DAY, 0);
            calActual.set(Calendar.MINUTE, 0);
            calActual.set(Calendar.SECOND, 0);
            calActual.set(Calendar.MILLISECOND, 0);

            fechaActual = calActual.getTime();

            // Calcular la diferencia en días
            long diffInMillies = fechaActual.getTime() - fecFinSinHora.getTime();
            long diasDiferencia = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            lista.setPorVencer(String.valueOf(diasDiferencia));

            // Si alertaVencimiento es null, tomar el valor de vencimiento del contrato o 10
            int alerta = (alertaVencimiento != null ? alertaVencimiento : (reg.getVencimiento() != null ? reg.getVencimiento() : 10)) * -1;
            boolean vencido = diasDiferencia >= alerta;
            lista.setVencido(vencido);

            lstResumen.add(lista);
        }
        Page<lstContract> result1 = new PageImpl<>(lstResumen, pageable, total);
        return result1;
    }

}
