package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.AdmContratoMacroDao;
import com.delta.deltanet.models.entity.AdmContratoMacro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AdmContratoMacroServiceImpl implements AdmContratoMacroService {

    @Autowired
    private AdmContratoMacroDao contratoMacroDao;

    @Override
    @Transactional
    public AdmContratoMacro crearContratoMacro(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            BigDecimal monto,
            Long monedaId,
            String descripcion,
            String usuario) {
        AdmContratoMacro contrato = new AdmContratoMacro();
        contrato.setFechaInicio(fechaInicio);
        contrato.setFechaFin(fechaFin);
        contrato.setDescripcion(descripcion);
        contrato.setMonto(monto);
        contrato.setMoneda(monedaId);
        contrato.setCreateUser(usuario);
        contrato.setCreateDate(LocalDateTime.now());
        contrato.setEstado("1");
        return contratoMacroDao.save(contrato);
    }

    @Override
    @Transactional(readOnly = true)
    public AdmContratoMacro obtenerContratoMacro(Long id) {
        Optional<AdmContratoMacro> contrato = contratoMacroDao.findById(id);
        return contrato.orElse(null);
    }

    @Override
    @Transactional
    public AdmContratoMacro actualizarContratoMacro(
            Long id,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            BigDecimal monto,
            Long monedaId,
            String descripcion,
            String usuario) {
        Optional<AdmContratoMacro> optionalContrato = contratoMacroDao.findById(id);

        if (optionalContrato.isPresent()) {
            AdmContratoMacro contrato = optionalContrato.get();
            contrato.setFechaInicio(fechaInicio);
            contrato.setFechaFin(fechaFin);
            contrato.setDescripcion(descripcion);
            contrato.setMonto(monto);
            contrato.setMoneda(monedaId);
            contrato.setUpdateUser(usuario);
            contrato.setUpdateDate(LocalDateTime.now());
            return contratoMacroDao.save(contrato);
        }

        return null;
    }

    @Override
    @Transactional
    public String cambiarEstadoContratoMacro(Long id, String usuario) {
        Optional<AdmContratoMacro> optionalContrato = contratoMacroDao.findById(id);

        if (optionalContrato.isPresent()) {
            AdmContratoMacro contrato = optionalContrato.get();
            String mensaje;

            if ("1".equals(contrato.getEstado())) {
                contrato.setEstado("2");
                mensaje = "Desactivado exitosamente";
            } else if ("2".equals(contrato.getEstado())) {
                contrato.setEstado("1");
                mensaje = "Activado exitosamente";
            } else {
                throw new IllegalStateException("Estado desconocido");
            }

            contrato.setUpdateUser(usuario);
            contrato.setUpdateDate(LocalDateTime.now());
            contratoMacroDao.save(contrato);

            return mensaje;
        } else {
            throw new NoSuchElementException("Contrato no encontrado");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdmContratoMacro> buscarContratosMacro(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            @RequestParam(name = "moneda") Long monedaId) {
        return contratoMacroDao.findAll((Specification<AdmContratoMacro>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (fechaInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaInicio"), fechaInicio));
            }

            if (fechaFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fechaFin"), fechaFin));
            }

            if (monedaId != null && monedaId != 0) {
                predicates.add(criteriaBuilder.equal(root.get("moneda"), monedaId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}