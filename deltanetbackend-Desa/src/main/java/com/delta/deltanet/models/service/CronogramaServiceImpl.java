package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ICronogramaDao;
import com.delta.deltanet.models.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CronogramaServiceImpl implements ICronogramaService {

    @Autowired
    public ICronogramaDao cronogramaDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object> listaRelacionOutsourcing() {
        return cronogramaDao.listaRelacionOutsourcing();
    }

    @Override
    public List<lstCrono4> listadoFiltrado(Long idContrato, String descrip, Long idConsultor,
            Date fecIni, Date fecFin,
            String nroOC, Date fechaOC,
            Long estado, Integer nDias) {
        Date fecha = new Date();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cronograma> cq = cb.createQuery(Cronograma.class);
        Root<Cronograma> cronoRoot = cq.from(Cronograma.class);
        List<Predicate> predicates = new ArrayList<>();

        Join<Cronograma, Persona> personaJoin = cronoRoot.join("persona", JoinType.INNER);

        if (idContrato != null && idContrato != 0)
            predicates.add(cb.equal(cronoRoot.get("idContrato"), idContrato));
        if (descrip != null && !descrip.trim().isEmpty())
            predicates.add(cb.like(cb.lower(cronoRoot.get("descripcion")), "%" + descrip.toLowerCase() + "%"));
        if (idConsultor != null && idConsultor != 0)
            predicates.add((cb.equal(personaJoin.get("id"), idConsultor)));
        if (fecIni != null && fecFin != null)
            predicates.add(cb.between(cronoRoot.get("periodoFinal"), fecIni, fecFin));
        if (nroOC != null && !nroOC.trim().isEmpty())
            predicates.add(cb.like(cb.lower(cronoRoot.get("nroOC")), "%" + nroOC.toLowerCase() + "%"));
        if (fechaOC != null)
            predicates.add(cb.equal(cronoRoot.get("fechaOC"), fechaOC));
        if (estado != null)
            predicates.add(cb.equal(cronoRoot.get("estado"), estado));

        cq.where(predicates.toArray(new Predicate[0]));
        List<Cronograma> cronogramas = entityManager.createQuery(cq).getResultList();

        String porVencer = "---";
        boolean vencido;
        Calendar c = Calendar.getInstance();
        List<lstCrono4> lstResumen = new ArrayList<>();
        lstCrono4 lista;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        Date fecNew;
        for (Cronograma reg : cronogramas) {
            porVencer = "---";
            vencido = false;

            if (nDias != null) {
                c.setTime(reg.getPeriodoFinal());
                int dias = nDias * (-1);
                c.add(Calendar.DAY_OF_MONTH, dias);

                fecNew = c.getTime();
                if (fecha.after(fecNew) || fecha.equals(reg.getPeriodoFinal()) || fecha.equals(fecNew)) {
                    porVencer = "Afirmativo";
                    if (reg.getEstado() == 1 || reg.getEstado() == 1L)
                        vencido = true;
                }
            }

            Long idCto = reg.getIdContrato();
            String Empresa = ObtEmpresa(idCto);
            Date fecFinal = reg.getPeriodoFinal();
            long diffInMilles = fecha.getTime() - fecFinal.getTime();
            long diff = TimeUnit.DAYS.convert(diffInMilles, TimeUnit.MILLISECONDS);
            String strDif = "";
            if (diff == 0)
                strDif = "0";
            if (diff < 0)
                strDif = String.valueOf(diff);
            if (diff > 0)
                strDif = "+" + String.valueOf(diff);
            if (reg.getEstado() == 4 || reg.getEstado() == 4L)
                strDif = "---";

            lista = new lstCrono4();
            lista.setId(reg.getId());
            lista.setIdContrato(idCto);
            lista.setEmpresa(Empresa);
            lista.setDescripcion(reg.getDescripcion());
            lista.setIdConsultor(reg.getPersona().getId());
            if (reg.getPeriodoInicial() != null)
                lista.setPeriodoInicial(formatter.format(reg.getPeriodoInicial()));
            if (reg.getPeriodoFinal() != null)
                lista.setPeriodoFinal(formatter.format(reg.getPeriodoFinal()));
            lista.setNroOC(reg.getNroOC());
            if (reg.getFechaOC() != null)
                lista.setFechaOC(formatter.format(reg.getFechaOC()));
            lista.setEstado(reg.getEstado());
            lista.setFullname(reg.getPersona().getPerNat().getNombre() + " " +
                    reg.getPersona().getPerNat().getApePaterno() + " " +
                    reg.getPersona().getPerNat().getApeMaterno());
            if (nDias != null)
                lista.setPorVencer(porVencer);
            if (reg.getEstado() == 1)
                lista.setDescEstado("Activo");
            if (reg.getEstado() == 2)
                lista.setDescEstado("Desactivado");
            if (reg.getEstado() == 3)
                lista.setDescEstado("Creado");
            if (reg.getEstado() == 4)
                lista.setDescEstado("Terminado");
            lista.setPorVencer(strDif);
            lista.setVencido(vencido);
            lstResumen.add(lista);
        }
        return lstResumen;
    }

    @Override
    public Page<lstCrono4> listadoFiltrado(Long idContrato, String descrip, Long idConsultor, Date fecIni, Date fecFin,
            String nroOC, Date fechaOC, Long estado, Integer nDias, Integer column, String dir, Pageable pageable) {
        Date fecha = new Date();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cronograma> cq = cb.createQuery(Cronograma.class);
        Root<Cronograma> cronoRoot = cq.from(Cronograma.class);
        List<Predicate> predicates = new ArrayList<>();

        Join<Cronograma, Persona> personaJoin = cronoRoot.join("persona", JoinType.INNER);

        if (idContrato != null && idContrato != 0)
            predicates.add(cb.equal(cronoRoot.get("idContrato"), idContrato));
        if (descrip != null && !descrip.trim().isEmpty())
            predicates.add(cb.like(cb.lower(cronoRoot.get("descripcion")), "%" + descrip.toLowerCase() + "%"));
        if (idConsultor != null && idConsultor != 0)
            predicates.add((cb.equal(personaJoin.get("id"), idConsultor)));
        if (fecIni != null && fecFin != null)
            predicates.add(cb.between(cronoRoot.get("periodoFinal"), fecIni, fecFin));
        if (nroOC != null && !nroOC.trim().isEmpty())
            predicates.add(cb.like(cb.lower(cronoRoot.get("nroOC")), "%" + nroOC.toLowerCase() + "%"));
        if (fechaOC != null)
            predicates.add(cb.equal(cronoRoot.get("fechaOC"), fechaOC));
        if (estado != null)
            predicates.add(cb.equal(cronoRoot.get("estado"), estado));

        cq.where(predicates.toArray(new Predicate[0]));
        List<Cronograma> cronogramas = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();
        Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());

        String porVencer = "---";
        boolean vencido;
        Calendar c = Calendar.getInstance();
        List<lstCrono4> lstResumen = new ArrayList<>();
        lstCrono4 lista;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        Date fecNew;
        for (Cronograma reg : cronogramas) {
            porVencer = "---";
            vencido = false;

            if (nDias != null) {
                c.setTime(reg.getPeriodoFinal());
                int dias = nDias * (-1);
                c.add(Calendar.DAY_OF_MONTH, dias);

                fecNew = c.getTime();
                if (fecha.after(fecNew) || fecha.equals(reg.getPeriodoFinal()) || fecha.equals(fecNew)) {
                    porVencer = "Afirmativo";
                    if (reg.getEstado() == 1 || reg.getEstado() == 1L)
                        vencido = true;
                }
            }

            Long idCto = reg.getIdContrato();
            String Empresa = ObtEmpresa(idCto);
            Date fecFinal = reg.getPeriodoFinal();
            long diffInMilles = fecha.getTime() - fecFinal.getTime();
            long diff = TimeUnit.DAYS.convert(diffInMilles, TimeUnit.MILLISECONDS);
            String strDif = "";
            if (diff == 0)
                strDif = "0";
            if (diff < 0)
                strDif = String.valueOf(diff);
            if (diff > 0)
                strDif = "+" + String.valueOf(diff);
            if (reg.getEstado() == 4 || reg.getEstado() == 4L)
                strDif = "---";

            lista = new lstCrono4();
            lista.setId(reg.getId());
            lista.setIdContrato(idCto);
            lista.setEmpresa(Empresa);
            lista.setDescripcion(reg.getDescripcion());
            lista.setIdConsultor(reg.getPersona().getId());
            if (reg.getPeriodoInicial() != null)
                lista.setPeriodoInicial(formatter.format(reg.getPeriodoInicial()));
            if (reg.getPeriodoFinal() != null)
                lista.setPeriodoFinal(formatter.format(reg.getPeriodoFinal()));
            lista.setNroOC(reg.getNroOC());
            if (reg.getFechaOC() != null)
                lista.setFechaOC(formatter.format(reg.getFechaOC()));
            lista.setEstado(reg.getEstado());
            lista.setFullname(reg.getPersona().getPerNat().getNombre() + " " +
                    reg.getPersona().getPerNat().getApePaterno() + " " +
                    reg.getPersona().getPerNat().getApeMaterno());
            if (nDias != null)
                lista.setPorVencer(porVencer);
            if (reg.getEstado() == 1)
                lista.setDescEstado("Activo");
            if (reg.getEstado() == 2)
                lista.setDescEstado("Desactivado");
            if (reg.getEstado() == 3)
                lista.setDescEstado("Creado");
            if (reg.getEstado() == 4)
                lista.setDescEstado("Terminado");
            lista.setPorVencer(strDif);
            lista.setVencido(vencido);
            lstResumen.add(lista);
        }
        Page<lstCrono4> result1 = new PageImpl<>(lstResumen, pageable, total);
        return result1;
    }

    @Override
    public List<Object> listaCronoPorIdPer(Long id) {
        return cronogramaDao.listaCronoPorIdPer(id);
    }

    @Override
    public List<Object> ListaPorVencer() {
        return cronogramaDao.listaPorVencer();
    }

    @Override
    public String ObtEmpresa(Long IdContrato) {
        return cronogramaDao.ObtEmpresa(IdContrato);
    }

    @Override
    public String ObtUsuario(Long IdContrato) {
        return cronogramaDao.ObtUsuario(IdContrato);
    }

    @Override
    public String ObtTipoDocumento(Long IdContrato) {
        return cronogramaDao.ObtTipoDocumento(IdContrato);
    }

    @Override
    public String ObtNroDocumento(Long IdContrato) {
        return cronogramaDao.ObtNroDocumento(IdContrato);
    }


    @Override
    public Object BuscaId(Long id) {
        return cronogramaDao.BuscaId(id);
    }

    @Override
    public Cronograma findById(Long id) {
        return cronogramaDao.findById(id).orElse(null);
    }

    @Override
    public Cronograma save(Cronograma cronograma) {
        return cronogramaDao.save(cronograma);
    }

    @Override
    public List<Object> BusquedaFiltrada() {
        return cronogramaDao.BusquedaFiltrada();
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, tipoDoc);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, tipoDoc, nroDoc);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, String nombres) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, tipoDoc, nroDoc, nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, String nombres,
            Long estado) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, tipoDoc, nroDoc, nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada(Long tipoDoc) {
        return cronogramaDao.BusquedaFiltrada(tipoDoc);
    }

    @Override
    public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc) {
        return cronogramaDao.BusquedaFiltrada(tipoDoc, nroDoc);
    }

    @Override
    public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, String nombres) {
        return cronogramaDao.BusquedaFiltrada(tipoDoc, nroDoc, nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, String nombres, Long estado) {
        return cronogramaDao.BusquedaFiltrada(tipoDoc, nroDoc, nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada(String nroDoc) {
        return cronogramaDao.BusquedaFiltrada(nroDoc);
    }

    @Override
    public List<Object> BusquedaFiltrada(String nroDoc, String nombres) {
        return cronogramaDao.BusquedaFiltrada(nroDoc, nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada(String nroDoc, String nombres, Long estado) {
        return cronogramaDao.BusquedaFiltrada(nroDoc, nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(String nombres) {
        return cronogramaDao.BusquedaFiltrada1(nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada(String nombres, Long estado) {
        return cronogramaDao.BusquedaFiltrada(nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Long estado) {
        return cronogramaDao.BusquedaFiltrada1(estado);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, nroDoc);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, String nombres) {
        return cronogramaDao.BusquedaFiltrada1(fecIni, fecFin, nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long estado) {
        return cronogramaDao.BusquedaFiltrada1(fecIni, fecFin, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Long tipoDoc, String nombres) {
        return cronogramaDao.BusquedaFiltrada1(tipoDoc, nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada(Long tipoDoc, Long estado) {
        return cronogramaDao.BusquedaFiltrada(tipoDoc, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(String nroDoc, Long estado) {
        return cronogramaDao.BusquedaFiltrada1(nroDoc, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long tipoDoc, String nombres) {
        return cronogramaDao.BusquedaFiltrada1(fecIni, fecFin, tipoDoc, nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, Long estado) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, tipoDoc, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, String nombres) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, nroDoc, nombres);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, Long estado) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, nroDoc, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, String nombres, Long estado) {
        return cronogramaDao.BusquedaFiltrada1(fecIni, fecFin, nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, Long estado) {
        return cronogramaDao.BusquedaFiltrada(tipoDoc, nroDoc, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Long tipoDoc, String nombres, Long estado) {
        return cronogramaDao.BusquedaFiltrada1(tipoDoc, nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, String nombres, Long estado) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, nroDoc, nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nombres, Long estado) {
        return cronogramaDao.BusquedaFiltrada(fecIni, fecFin, tipoDoc, nombres, estado);
    }

    @Override
    public List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, Long estado) {
        return cronogramaDao.BusquedaFiltrada1(fecIni, fecFin, tipoDoc, nroDoc, estado);
    }

    @Override
    public List<Object> listaRecursos() {
        return cronogramaDao.listaRecursos();
    }

    @Override
    public List<Cronograma> BusquedaCronogramas(Long contactID) {
        return cronogramaDao.BusquedaCronogramas(contactID);
    }


    @Override
    public List<Cronograma> findByPersonaAndEstado(Long idPersona, Long estado) {
        return cronogramaDao.findByPersonaIdAndEstado(idPersona, estado);
    }

    @Override
    public List<Cronograma> findByEstado(Long estado) {
        return cronogramaDao.findByEstado(estado);
    }
}
