package com.delta.deltanet.models.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.delta.deltanet.models.entity.Contrato;

public interface ContratoService {

    Page<Contrato> searchContratos(Long idRelacion, Long idTipoContrato, Long idTipoServicio, Long idFormaPago,
            String descripcion, Date fecIniRng1, Date fecIniRng2, Date fecFinRng1, Date fecFinRng2, Long estado,
            Integer swPag, Integer column, String dir, Pageable pageable);
}
