package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.ContratoContacto;
import com.delta.deltanet.models.entity.lstContract;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IContratoContactoService {
    ContratoContacto save(ContratoContacto contratoContacto);

    Optional<ContratoContacto> busca(Long idContrato, Long idContacto);

    void delete(Long id);

    List<ContratoContacto> getContactos(Long idContrato);

    List<lstContract> listadoFiltrado(String descrip,
            Date fecIni,
            Date fecFin,
            Long estado,
            Integer nDias);

    Page<lstContract> listadoFiltrado(String descrip,
            Date fecIni,
            Date fecFin,
            Long estado,
            Integer nDias,
            Integer column,
            String dir,
            Pageable pageable);
}
